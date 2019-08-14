#!/bin/sh
echo "准备安装$RPM_PACKAGE_NAME"
if [ ! -d "/opt/$RPM_PACKAGE_NAME/" ];then
mkdir /opt/$RPM_PACKAGE_NAME/
echo "准备完成"
else
echo "文件夹已存在，准备完成"
fi