#!/bin/bash
deploy_type=$1
tomcat_home=/ecs/tomcat_nginx/tomcat8910

rm -rf /tmp/shopwap
unzip -oq /tmp/shop-wap.war -d /tmp/shopwap

if [[ !$deploy_type || "$deploy_type"x == "full"x ]]; then
    pkill -f $tomcat_home/bin
fi

if [ "$deploy_type"x == "full"x ]; then
    rm -rf $tomcat_home/shopwap_bak
    mv -f $tomcat_home/webapps/shopwap $tomcat_home/shopwap_bak

    mv -f /tmp/shopwap $tomcat_home/webapps
elif [ !$deploy_type ]; then
    rm -rf $tomcat_home/shopwap_bak

    cd $tomcat_home/webapps/shopwap/
    rm -rf WEB-INF/classes/com
    cp -rf /tmp/shopwap/WEB-INF/classes/com WEB-INF/classes/com
    rm -rf WEB-INF/views
    cp -rf /tmp/shopwap/WEB-INF/views WEB-INF/views
fi

cd $tomcat_home/webapps/shopwap/
zip -oq -r /tmp/shopwap_static.zip static

#scp拷贝static压缩包
expect -c "
   set timeout 1;
   spawn scp -r /tmp/shopwap_static.zip ruut@10.154.73.87:/ecslog/static/shop_wap/shopwap ;
   expect {
       yes/no { send \"yes\r\"; exp_continue }
       *assword* { send \"Ruut#123\r\" }
   } ;
   expect ruut@*  { send exit\r } ;

   spawn scp -r /tmp/shopwap_static.zip ruut@10.159.98.79:/app/static/shop_wap/shopwap ;
   expect {
       yes/no { send \"yes\r\"; exp_continue }
       *assword* { send \"Ruut#123\r\" }
   } ;
   expect ruut@*  { send exit\r } ;

   spawn scp -r /tmp/shopwap_static.zip ruut@10.159.98.80:/app/static/shop_wap/shopwap ;
   expect {
       yes/no { send \"yes\r\"; exp_continue }
       *assword* { send \"Ruut#123\r\" }
   } ;
   expect ruut@*  { send exit\r } ;
"

#ssh登录远程备份static并解压新的static包
expect -c "
   set timeout 1;
   spawn ssh ruut@10.154.73.87  ;
   expect {
       yes/no { send \"yes\r\"; exp_continue }
       *assword* { send \"Ruut#123\r\" }
   } ;
   expect ruut@*  { send \"cd /ecslog/static/shop_wap/shopwap/\r\" }  ;
   expect ruut@*  { send \"rm -rf static_bak\r\" }  ;
   expect ruut@*  { send \"mv -f static static_bak\r\" }  ;
   expect ruut@*  { send \" unzip -oq shopwap_static.zip \r\" }  ;
   expect ruut@*  { send \" ll \r\" }  ;
   expect ruut@*  { send exit\r } ;

   spawn ssh ruut@10.159.98.79  ;
   expect {
       yes/no { send \"yes\r\"; exp_continue }
       *assword* { send \"Ruut#123\r\" }
   } ;
   expect ruut@*  { send \"cd /app/static/shop_wap/shopwap/\r\" }  ;
   expect ruut@*  { send \"rm -rf static\r\" }  ;
   expect ruut@*  { send \" unzip -oq shopwap_static.zip \r\" }  ;
   expect ruut@*  { send \" ll \r\" }  ;
   expect ruut@*  { send exit\r } ;

   spawn ssh ruut@10.159.98.80  ;
   expect {
       yes/no { send \"yes\r\"; exp_continue }
       *assword* { send \"Ruut#123\r\" }
   } ;
   expect ruut@*  { send \"cd /app/static/shop_wap/shopwap/\r\" }  ;
   expect ruut@*  { send \"rm -rf static\r\" }  ;
   expect ruut@*  { send \" unzip -oq shopwap_static.zip \r\" }  ;
   expect ruut@*  { send \" ll \r\" }  ;
   expect ruut@*  { send exit\r } ;
"

echo "shopwap is OK!"

if [[ !$deploy_type || "$deploy_type"x == "full"x ]]; then
    echo "shopwap is starting!"
    set timeout 10
    cd $tomcat_home/bin/
    startup.sh
    tail -f $tomcat_home/logs/catalina.out
fi