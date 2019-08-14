#!/bin/sh
if [ "$1" = 1 ];then
  echo "-----------------------------开始安装-----------------------------"
else
  echo "-----------------------------开始更新-----------------------------"
  if [ -f "/etc/init.d/$RPM_PACKAGE_NAME" ];then
    service $RPM_PACKAGE_NAME stop
    rm -rf /etc/init.d/$RPM_PACKAGE_NAME
    rm -rf /opt/$RPM_PACKAGE_NAME/$RPM_PACKAGE_NAME.jar
  fi
fi
ln -s /opt/$RPM_PACKAGE_NAME/$RPM_PACKAGE_NAME.jar /etc/init.d/$RPM_PACKAGE_NAME
service $RPM_PACKAGE_NAME start
echo "--------------------------------结束-----------------------------------"
