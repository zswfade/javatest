package com.example.aliyuniot;

import android.graphics.Bitmap;
import android.util.Log;

import com.aliyun.iot20180120.Client;
import com.aliyun.iot20180120.models.PubRequest;
import com.aliyun.iot20180120.models.PubResponse;
import com.aliyun.iot20180120.models.QueryDeviceDesiredPropertyRequest;
import com.aliyun.iot20180120.models.QueryDeviceDesiredPropertyResponse;
import com.aliyun.iot20180120.models.QueryDeviceStatisticsRequest;
import com.aliyun.iot20180120.models.QueryDeviceStatisticsResponse;
import com.aliyun.iot20180120.models.SetDeviceDesiredPropertyRequest;
import com.aliyun.iot20180120.models.SetDeviceDesiredPropertyResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;//同步线程管理工具

public class IotTool {
    private String AccessKey=null;//非常重要不要告诉任何人
    private String AccessId=null;//非常重要，不要告诉任何人
    private String EndPoint=null;
    private String IotInStanceId=null;
    public Client client=null;
    public Thread requestThread=null;
    public Thread responeThread=null;
    public req_Run run1=null;
    public req_Run run2=null;
    public PubRequest request=null;
    public Queue<PubRequest> req_Queue=null;
    public  Queue<PubResponse> res_Queue=null;
    //访问id，访问密码，访问结点，访问物模型实例
    IotTool(String accessKey, String accessId, String endPoint,String iotInStanceId) {
        this.AccessId=accessId;
        this.AccessKey=accessKey;
        this.EndPoint=endPoint;
        this.IotInStanceId=iotInStanceId;
    }
    //初始化网络客户端
    void Init_Iot() throws Exception {
      Config config=new Config()
              .setAccessKeyId(AccessId)
              .setAccessKeySecret(AccessKey);
      config.endpoint=EndPoint;
      client=new Client(config);
    }
    //启动请求线程
    void start_requset(){
         requestThread=new Thread(run1,"thread1");
         requestThread.start();
    }
    //启动响应线程
    void start_response(){
          responeThread=new Thread(run2,"thread2");
          responeThread.start();
    }
    class req_Run implements Runnable{

        @Override
        public void run() {
//此处实现操作逻
            while(true) {
                if (!req_Queue.isEmpty()) {
                   PubRequest queue=req_Queue.poll();
                    try {
                        PubResponse queue1=client.pub(queue);
                        res_Queue.offer(queue1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    class res_Run implements Runnable{
        @Override
        public void run() {
   //此处实现数据接收处理逻辑
            while(true){
                for (PubResponse res:res_Queue) {
                    if(res.getBody().getSuccess()){
                      Log.d("iot","topic pub success");
                    }
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //查询期望属性
    void query_DesiredProperty(String property) throws Exception {
         if(client!=null)
         {
             QueryDeviceDesiredPropertyRequest requset= new QueryDeviceDesiredPropertyRequest();
             requset.setIdentifier(Collections.singletonList(property));
             RuntimeOptions time=new RuntimeOptions();
             QueryDeviceDesiredPropertyResponse res=client.queryDeviceDesiredPropertyWithOptions(requset,time);
             System.out.println(new Gson().toJson(res));
         }
         else
             Log.d("iot","please init client!");
    }
    //设置期望属性
    /*
    * porperty:{"par":1} 要设置的期望对象那个
    * version:{"par":0} 期望版本，如果期望不一致会造成冲突
    * */
    void set_DesiredProperty(String property,String version) throws Exception {
         if(client!=null){
             SetDeviceDesiredPropertyRequest request=new SetDeviceDesiredPropertyRequest()
                     .setItems(property)
                     .setVersions(version);
             RuntimeOptions time=new RuntimeOptions();
             SetDeviceDesiredPropertyResponse res=client.setDeviceDesiredPropertyWithOptions(request,time);
             System.out.println(new Gson().toJson(res));
         }
         else
             Log.d("iot","please init client!");
    }
    /*此api官方已经删除*/
    void clear_DesiredProperty(String property)
    {
        if(client!=null){

        }
    }
    /*
    *查询当前产品的设备数量
    */
    void query_DeviceStatic() throws Exception {
      if(client!=null){
          QueryDeviceStatisticsRequest request=new QueryDeviceStatisticsRequest();
          RuntimeOptions time=new RuntimeOptions();
          QueryDeviceStatisticsResponse res=client.queryDeviceStatisticsWithOptions(request,time);
          System.out.println(new Gson().toJson(res));
      }
    }
    void init_PubRequest(String topic,String devicename,String prokduct,String content,int qos)
    {
       request=new PubRequest()
       .setIotInstanceId(IotInStanceId)
       .setProductKey(prokduct)
       .setDeviceName(devicename)
               .setTopicFullName(topic)
               .setMessageContent(content)
               .setQos(qos);
       req_Queue.offer(request);
    }
}
