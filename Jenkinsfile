node{
	def server = Artifactory.server 'ART'
   	def rtMaven = Artifactory.newMavenBuild()
	def mvnHome = tool name: 'M2_HOME', type: 'maven'
   	def buildInfo
	def branch = 'master'
	
  stage('checkout'){
	git branch: branch, credentialsId: 'b92211ca-9a5d-4ad0-b540-b4222e34acd2', url: 'git@github.com:mosip-guest/Technoforte.git'
  }
  stage('--------- Artifactory configuration ----------------') {
  /*
  JFrog artifactory configuration
  */
  rtMaven.tool = 'M2_HOME' // Tool name from Jenkins configuration
  rtMaven.deployer releaseRepo: 'libs-release-local', snapshotRepo: 'libs-snapshot-local', server: server
  rtMaven.resolver releaseRepo: 'libs-release', snapshotRepo: 'libs-snapshot', server: server
  buildInfo = Artifactory.newBuildInfo()
  buildInfo.env.capture = true
 }
 stage('---------- mvn-clean-install and push to artifactory  ---------------') {

  rtMaven.run pom: 'pom.xml', goals: 'clean install -DskipTests', buildInfo: buildInfo

  
 }
  stage('----------- Publish build info -------------') {
  /*
  Publishing build info to Artifcatory (JFrog)
  */
  server.publishBuildInfo buildInfo
 }
  }
