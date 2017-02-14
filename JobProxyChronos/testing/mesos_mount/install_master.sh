#!/bin/sh
#export DEBIAN_FRONTEND=noninteractive

sudo apt-key adv --keyserver keyserver.ubuntu.com --recv E56151BF
DISTRO=$(lsb_release -is | tr '[:upper:]' '[:lower:]')
CODENAME=$(lsb_release -cs)
echo "deb http://repos.mesosphere.io/${DISTRO} ${CODENAME} main" | sudo tee /etc/apt/sources.list.d/mesosphere.list

sudo apt-get -y update
sudo apt-get install -y python2.7
sudo apt-get install -y docker.io
sudo apt-get install -y mesos zookeeperd zookeeper zookeeper-bin

#purge java9 due to incompatibility with some packages
sudo apt purge -y openjdk-9-jre-headless

#config ZK
sudo bash -c 'echo "zk://10.0.0.2:2181/mesos" > /etc/mesos/zk'
sudo bash -c 'echo "1" > /etc/zookeeper/conf/myid'
sudo bash -c 'echo "server.1=10.0.0.2:2888:3888" >> /etc/zookeeper/conf/zoo.cfg'

#config mesos Master
sudo bash -c 'echo "1" > /etc/mesos-master/quorum'
sudo bash -c 'echo "10.0.0.2" > /etc/mesos-master/ip'
sudo cp /etc/mesos-master/ip /etc/mesos-master/hostname


#stop mesos services
#sudo stop mesos-slave
echo manual | sudo tee /etc/init/mesos-slave.override

#restart services

sudo /usr/share/zookeeper/bin/zkServer.sh restart
sleep 10
sudo service mesos-master start
sleep 10
sudo docker run -d --net=host -e PORT0=8081 -e PORT1=8082 mesosphere/chronos:v3.0.0 --zk_hosts 10.0.0.2:2181 --master zk://10.0.0.2:2181/mesos

#sudo bash -c 'echo "1" > /etc/mesos-master/quorum'
#sudo bash -c 'echo "1" > /etc/mesos-master/quorum'
