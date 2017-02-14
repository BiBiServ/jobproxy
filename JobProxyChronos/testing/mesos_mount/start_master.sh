#!/bin/bash

nohup bash -c "sudo /mesos/mesos-1.1.0/build/bin/mesos-master.sh --ip=10.0.0.2 --work_dir=/var/lib/mesos >> /mesos/log_$(hostname) &" >> /mesos/log_$(hostname)

exit
