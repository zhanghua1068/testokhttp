package tunnel.com.myhttp0807;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class WYDDataRequire 
{	
	//网络请求超时时间
	private int timeout=30000;
	//网络请求成功的TAG
	private static final int DATAREQUIRE_SUCCESS=0;
	//网络请求失败的TAG
	private static final int DATAREQUIRE_FAILED=1;
	private DataRequireListener listener;
	@SuppressLint("HandlerLeak")
	private Handler h=new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what) 
			{
			case DATAREQUIRE_SUCCESS:
				listener.data_RequireFinidhed(msg.obj.toString());
				break;
			case DATAREQUIRE_FAILED:
				listener.data_RequireFailed("请求失败！");
				break;
			default:
				break;
			}
		}
	};
	public void getData(final String servicesName,final String functionName,final String []nameArray,final Object []keyArray,DataRequireListener listener)
	{
		this.listener=listener;
		new Thread()
		{
			public void run()
			{
				SoapObject soapObject = new SoapObject("https://192.168.1.110:8443/"+servicesName,functionName);
				for(int i=0;i<nameArray.length;i++)
				{
					soapObject.addProperty(nameArray[i],keyArray[i]);
				}
				SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelop.dotNet = true;
				envelop.setOutputSoapObject(soapObject);
				WYDHttpTransportSE httpSE = new WYDHttpTransportSE("https://192.168.1.110:8443/AijiaWebSvc/services/"+servicesName,timeout);
				httpSE.setListener(httpTransportSeListener);
				try 
				{
					httpSE.call("https://192.168.1.110:8443/"+servicesName+"/"+functionName, envelop);
					
					Object resultObj = (Object) envelop.getResponse();
					
					Message msg=new Message();
					msg.what=DATAREQUIRE_SUCCESS;
					msg.obj=resultObj.toString();
					h.sendMessage(msg);
				} 
				catch (IOException e)
				{
					h.sendEmptyMessage(DATAREQUIRE_FAILED);
					e.printStackTrace();
				} catch (XmlPullParserException e)
				{
					h.sendEmptyMessage(DATAREQUIRE_FAILED);
					e.printStackTrace();
				}
			}
		}.start();
	}
	/**
	 * 请求的进度
	 */
	WYDHttpTransportSE.WYDHttpTransportSeListener httpTransportSeListener=new WYDHttpTransportSE.WYDHttpTransportSeListener()
	{
		@Override
		public void publishProgress(int progress) 
		{
			listener.data_RequireProgress(progress);
		}
	};
	 public interface DataRequireListener
	 {
		 public void data_RequireFinidhed(String str);
		 public void data_RequireFailed(String error);
		 public void data_RequireProgress(int progress);
	 }
}
