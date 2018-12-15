***********前提条件******************

172.16.203.100 这个机器是一个文件服务器，保证wget的时候，能从目录/soft/jdk-7u45-linux-x64.tar.gz 取到文件

使用时启动boot.sh

首先 执行ssh_copy_id_to_all 函数执行过程是将本机的免密登录公钥  传到其他server的过程

然后执行循环，将install.sh scp到其他机器上
远程登录，执行install.sh脚本


install.sh 执行过程
0.安装wget命令
1.从文件服务器下载到jdk
2.解压到/usr/local
3.写入环境变量
