#!/bin/sh

sudo apt-key adv --keyserver keyserver.ubuntu.com --recv E56151BF
DISTRO=$(lsb_release -is | tr '[:upper:]' '[:lower:]')
CODENAME=$(lsb_release -cs)
echo "deb http://repos.mesosphere.io/${DISTRO} ${CODENAME} main" | sudo tee /etc/apt/sources.list.d/mesosphere.list
sleep 1

sudo apt-get -y update
sleep 1

sudo apt-get install -y python2.7
sudo apt-get install -y docker.io
sudo apt-get install -y mesos
sleep 1
#purge java9
#sudo apt purge -y openjdk-9-jre-headless

#disable zookeeper
#sudo /usr/share/zookeeper/bin/zkServer.sh stop
#echo manual | sudo tee /etc/init/mesos-master.override

#configure zookeeper
sudo bash -c 'echo "zk://10.0.0.2:2181/mesos" > /etc/mesos/zk'
sleep 1
#disable master
sudo service mesos-master stop
echo manual | sudo tee /etc/init/mesos-master.override

OWN_IP=$(ifconfig | perl -nle 's/dr:(\S+)/print $1/e' | grep "10.0.0.*")

#configure mesos slave

echo $OWN_IP | sudo tee /etc/mesos-slave/ip
sudo cp /etc/mesos-slave/ip /etc/mesos-slave/hostname
sleep 1
sudo service mesos-slave start
