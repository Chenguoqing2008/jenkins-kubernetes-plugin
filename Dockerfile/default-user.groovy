import jenkins.model.*
import hudson.security.*

def env = System.getenv()

def jenkins = Jenkins.getInstance()
jenkins.setSecurityRealm(new HudsonPrivateSecurityRealm(false))
jenkins.setAuthorizationStrategy(new GlobalMatrixAuthorizationStrategy())

def defaultadmin = jenkins.getSecurityRealm().createAccount(env.JENKINS_USER, env.JENKINS_PASS)
defaultadmin.save()

def admin1 = jenkins.getSecurityRealm().createAccount(env.JENKINS_ADMINUSER1, env.JENKINS_DEFAULTPASSWD)
admin1.save()

jenkins.getAuthorizationStrategy().add(Jenkins.ADMINISTER, env.JENKINS_USER)
jenkins.getAuthorizationStrategy().add(Jenkins.ADMINISTER, env.JENKINS_ADMINUSER1)
jenkins.getAuthorizationStrategy().add(Jenkins.ADMINISTER, env.JENKINS_ADMINUSER2)
jenkins.save()
