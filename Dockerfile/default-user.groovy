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

def admin2 = jenkins.getSecurityRealm().createAccount(env.JENKINS_ADMINUSER2, env.JENKINS_DEFAULTPASSWD)
admin2.save()

def user1 = jenkins.getSecurityRealm().createAccount(env.JENKINS_NORMALUSER1, env.JENKINS_DEFAULTPASSWD)
user1.save()

def user2 = jenkins.getSecurityRealm().createAccount(env.JENKINS_NORMALUSER2, env.JENKINS_DEFAULTPASSWD)
user2.save()

def user3 = jenkins.getSecurityRealm().createAccount(env.JENKINS_NORMALUSER3, env.JENKINS_DEFAULTPASSWD)
user3.save()

def user4 = jenkins.getSecurityRealm().createAccount(env.JENKINS_NORMALUSER4, env.JENKINS_DEFAULTPASSWD)
user4.save()

def user5 = jenkins.getSecurityRealm().createAccount(env.JENKINS_NORMALUSER5, env.JENKINS_DEFAULTPASSWD)
user5.save()

def user6 = jenkins.getSecurityRealm().createAccount(env.JENKINS_NORMALUSER6, env.JENKINS_DEFAULTPASSWD)
user6.save()

def user7 = jenkins.getSecurityRealm().createAccount(env.JENKINS_NORMALUSER7, env.JENKINS_DEFAULTPASSWD)
user7.save()

def user8 = jenkins.getSecurityRealm().createAccount(env.JENKINS_NORMALUSER8, env.JENKINS_DEFAULTPASSWD)
user8.save()

def user9 = jenkins.getSecurityRealm().createAccount(env.JENKINS_NORMALUSER9, env.JENKINS_DEFAULTPASSWD)
user9.save()

def user10 = jenkins.getSecurityRealm().createAccount(env.JENKINS_NORMALUSER10, env.JENKINS_DEFAULTPASSWD)
user10.save()

def user11 = jenkins.getSecurityRealm().createAccount(env.JENKINS_NORMALUSER11, env.JENKINS_DEFAULTPASSWD)
user11.save()

def user12 = jenkins.getSecurityRealm().createAccount(env.JENKINS_NORMALUSER12, env.JENKINS_DEFAULTPASSWD)
user12.save()

def user13 = jenkins.getSecurityRealm().createAccount(env.JENKINS_NORMALUSER13, env.JENKINS_DEFAULTPASSWD)
user13.save()


jenkins.getAuthorizationStrategy().add(Jenkins.ADMINISTER, env.JENKINS_USER)
jenkins.getAuthorizationStrategy().add(Jenkins.ADMINISTER, env.JENKINS_ADMINUSER1)
jenkins.getAuthorizationStrategy().add(Jenkins.ADMINISTER, env.JENKINS_ADMINUSER2)
jenkins.save()