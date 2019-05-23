#! /usr/bin/python

# This script is to delete the redundency files of jobs folder of jenkins,
# put the script outside of the jobs folder and pass "jobs" as parameter then it will
# do the dirty job for you.

import sys
import os
import shutil

g = os.getcwd()
os.chdir(sys.argv[1])
targetdir = os.getcwd()
jobs_name_dir = []

for item in os.listdir(targetdir):
	if os.path.isdir(item):
		jobs_name_dir.append(item)
		
for path,dir_list,file_list in os.walk(targetdir):
	for d in dir_list:
		if d not in jobs_name_dir:
			cmd_rm_dir = "rm -rf " + os.path.join(path,'builds')
			print(cmd_rm_dir)
			os.system(cmd_rm_dir)

for path,dir_list,file_list in os.walk(targetdir):
	for f in file_list:
		if f != 'config.xml':
			cmd_rm_file = "rm -rf " + os.path.join(path,f)
			print(cmd_rm_file)
			os.system(cmd_rm_file)
			

	
