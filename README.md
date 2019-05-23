# Dockerfile description  
1. jenkins-master-dockerfile build image that will run in jenkins master;  
2. jenkins-slave-dockerfile-python2 build image that will run python2 script in jenkins slave;  
3. jenkins-slave-dockerfile-python3 build image that will run python3 script and OPENCV in jenkins slave;  
4. jenkins-slave-dockerfile-python3-catpture build image that will run python3 script and ffmpeg in jenkins slave;  

# About jobs config files  
1. Dockerfile\config\azure_jobs is previous Azure jenkins jobs;  
2. Dockerfile\config\jobs\active_jobs is the active jobs;  
3. Dockerfile\config\jobs\inactive_jobs is the inactive jobs;  


# Registry docker images
kubectl create secret docker-registry traffic-acr --docker-server traffic1.azurecr.io --docker-username traffic1 --docker-password e4Wi9=73SRZiUQRb7cBxPFoNC/6n/1Ay --docker-email chao.lee@percolata.com


# Download, Upload Jenkins config files from Jenkins Kubernetes master  

### Download Jenkins master config files  

As we are currently running Jenkins on Azure and have done all the configurations.   
We need to download the Jenkins jobs foler files and prepare to upload to Google clound.  

   1. get the jenkins master pods name;  
      kubectl get pods

      NAME                       |   READY   |  STATUS  |  RESTARTS  |  AGE  |  
      jenkins-65fdbffcd7-w2frh   |   1/1     |  Running |   0        |  6d1h | 

      The jenkins jobs config file should be in folder /var/jenkins_home/jobs by default;  
   2. at the console run command to cp the jobs configuration files;  
      kubectl cp jenkins-65fdbffcd7-w2frh:/var/jenkins_home/jobs/* /mnt/e/config  
      /mnt/e/config is the console folder that you want to save the configuration files  
   3. Check the jobs folder and filter unnecessary files;  
      put the cleaner.py  outside of jobs folder  
      run python cleaner.py jobs  
      Then the script will clearn all the redundancy file in jobs folder and only have the config.xml files.  
   
### Upload Jenkins master config files to Google Cloud Jenkins  

    When the Jenkins Google Cloud is ready we shall upload the jobs config files into Jenkins.  
    At console run command  
       kubectl get pods  
    You can get the master pods name for example jenkins-xxxx  
    Then run command
       kubectl cp  ${jobsfolder}  jenkins-xxxx:/var/jenkins_home/jobs/*(please keep in mind that ${jobsfolder} must be absolute path)

### Backup the config files  

    At Jenkins jobs console run job backup then jenkins config files will back up to bitbucket. Next I will creat a piple to do the backup and filter jobs files.  



# Create Jenkins master image and push to repo:  
1. docker build -t $imagename:tag$  -f jenkins-dockerfile; (in Dockerfile folder)
2. docker login remote repo;
3. docker push $imagename$:$tag$;    
Then the Jenkins image is in the repo.


# Create Jenkins slave image and push to repo:  
1. docker build -t $imagename:tag$  -f jenkins-slave-dockerfile; (in Dockerfile folder)
2. docker login remote repo;
3. docker push $imagename$:$tag$;    
Then the Jenkins image is in the repo.


# Set up Kubernetes deployment:

1. Modify jenkins-deployment.yaml; (in k8s-jenkins-deployment folder)
   image: robinchen007/percolatarobin:4.0 to image name last step created. 
2. Deploy Jenkins;  
   kubectl apply -f jenkins-deployment.yaml (in k8s-jenkins-deployment folder)
3. check the Jenkins is successfully starts;  
   kubectl describe pod | grep jenkins  
   root@DESKTOP-NQNUCO2:/root# kubectl describe pods|grep jenkins
Name:               jenkins-74d97cbf4b-xd5fq  
Labels:             app=jenkins  
Controlled By:      ReplicaSet/jenkins-74d97cbf4b  
  jenkins:  
      /var/jenkins_home from jenkins-home (rw)  
  jenkins-home:  
  
# Expose Kubernetes service load balance IP address:
kubectl expose deployment ${appname} --type=LoadBalancer --name=${servicename}
  

# Set up Kubernetes service:
1. kubectl create -f jenkins-service.yaml; (in k8s-jenkins-deployment folder)
2. kubectl get service;
3. Try to visit Jenkins from http://staticIP:port


NAME            | TYPE             |  CLUSTER-IP    | EXTERNAL-IP      | PORT(S)            | AGE   |
--------------- | ---------------- | -------------- | ---------------- | ------------------ | ----- |
jenkins         | LoadBalancer     | 10.0.48.93     | 40.78.61.106     | 8080:32350/TCP     | 2d19h | 
kubernetes      | ClusterIP        | 10.0.0.1       | <none>           | 443/TCP            | 20d   |  



# Set up Kubernetes rbac:

  **kubectl apply -f fabric8-rbac.yaml**  (in k8s-jenkins-deployment folder)
  This step is very important for guarantee the communication betweeen pods.


# Grant user rights:  
1. log in as admin;
2. click "Manager Jenkins";
3. click "Configure Global Security", Authorization choose "Role-Based Strategy", save;
4. "Manage Jenkins" click "Manage and Assign Roles";
5. "Manage Roles" add "user" role grant its rights,
	see below table
6. "Assign Roles" add user to "Global roles"
	Grant greg and riche to admin role
	Others to "user" role

| overrall| Credentials | Agent |                            Job                           |    Run             |          View              |SCM |           Lockable      |  
| --------|-------------|-------|----------------------------------------------------------|--------------------|----------------------------|----|-------------------------|  
| read    | none        | none  |Build,Cancel,Configure,Delete,Discover,Move,Read,Workspace|Delete,Replay,Update|Configure,Create,Delete,Read|Tag |Resources  Reserve,Unlock|  


# Change number of executor  
Go to Manage Jenkins -> Configure System and increase the number of executor from 0 to n(>=1)


# Jenkins Slaves Configuration
1. Get Kubernest URL;  
kubectl cluster-info | grep master  
Kubernetes master is running at https://perco-84c5abc6.hcp.westus.azmk8s.io:443  
https://perco-84c5abc6.hcp.westus.azmk8s.io:443 is the Kubernest URL  

2. Get the Jenkins static URL;  
     1. get Jenkins pods name  
  kubectl get pod
  jenkins-74d97cbf4b-xd5fq   1/1     Running   0          2d20h  
     2. get jenkis pod IP  
  kubectl describe pods jenkins-74d97cbf4b-xd5fq  
  ... ...  
  IP:                 10.244.2.13  
  ... ...  
  

3. Config the Kubernetes URL in Cloud;  
“Manage Jenkins -> Configure System -> Cloud -> Kubernetes”   
fill in the ‘Kubernetes URL’   
‘Jenkins URL’  


4. Create Kubernetes Pod Template;  
    1.  Give Pod Template a name;  
    2.  Give Pod Template a Label(This will be used as Jenkins task configuration);  
    3.  Create a Container;  
     name: Container name;
     Docker image: jenkinsci/jnlp-slave  
     Working directory: /home/jenkins  

5. Config the sonarQube:
    1.“Manage Jenkins -> Configure System -> SonarQube servers”  
       fill in the "Name","Server URL","token"
    2.“Manage Jenkins -> Global tool configuration -> SonarQube Scanner”  
       Add SonarQube Scanner,fill in the "SonarQube Scanner",check "install automatic"

   
 
# How to get the Jenkins credentialsId
1. Jenkins -> Credentials, add global credential;  
2. Kind choose 'SSH Username with private key';  
3. Add Username 'percolata' and private key in folder /Dockerfile/id_rsa-bitbucket-backend;  
4. Then back to Jenkins Home page and click Credentials, the ID column is the credentialsId;  


# Create a pipline  
1. Create a pipeline add all the necessary parameters;  
2. Modify the resource you need to run the script in container;  
    resourceRequestCpu: '1000m',  
    resourceLimitCpu: '2000m',  
    resourceRequestMemory: '1000Mi',  
    resourceLimitMemory: '2000Mi' 
3. Modify the image name you will pull from docker registries such as myregistry.azurecr.io/robinchen007/jenkins-slave-base:2.9
4. Modify the Jenkinsfile set the proper bitbucket repo name,image name, credentialsId;  
5. Run the script and monitor the result;  

# Using docker registry to pull private image(Azure)
1. Get the secret of docker registry  
kubectl create secret docker-registry traffic-acr --docker-server traffic1.azurecr.io --docker-username traffic1 --docker-password e4Wi9=73SRZiUQRb7cBxPFoNC/6n/1Ay --docker-email chao.lee@percolata.com 
2. Azure all service 
Container registries
3. login in Azure docker  
docker login traffic1.azurecr.io -u traffic1 -p e4Wi9=73SRZiUQRb7cBxPFoNC/6n/1Ay
4. modify azure imgage in Jenkinsfile
traffic1.azurecr.io/robinchen007/jenkins-slave-base:2.3

basic usage:
push and pull
docker pull myregistry.azurecr.io/samples/nginx  
docker push myregistry.azurecr.io/samples/nginx 

# Using docker registry to pull private image(Google Cloud)  
1. Get the secret of docker registry   
kubectl create secret docker-registry gcr-json-key \
--docker-server=gcr.io \
--docker-username=_json_key \
--docker-password="$(cat ~/Percolata/Percolata-SZ-Production-c3a83ccde5c5.json)" \
--docker-email=chao.li@percolata.com  
2. docker login
docker login -u _json_key -p "$(cat faas-prod-f8012438ee7c.json)" https://gcr.io  
3. docker push gcr.io/faas-prod/sonarqube:latest  

doc: https://docs.google.com/document/d/10DV_UkjQk3NOlDtCHUgq9o6ukVbnT4TsaRaOaYUEpB0/edit#






  




