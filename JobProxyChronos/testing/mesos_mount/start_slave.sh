#!/bin/bash

nohup bash -c "sudo /mesos/mesos-1.1.0/build/bin/mesos-agent.sh --master=master:5050 --ip=$1 --work_dir=/var/lib/mesos >> /mesos/log_$(hostname) &" >> /mesos/log_$(hostname)

exit
