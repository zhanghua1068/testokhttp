package tunnel.com.myhttp0807;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class MainActivity extends Activity {
private Button btnRequest;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findView();
		test();
	}
	private void test()
	{
		AssetManager am=getAssets();
		try 
		{
			InputStream ins=am.open("server.cer");
			//读取证书
			CertificateFactory cerFactory=CertificateFactory.getInstance("X.506");
			Certificate cer=cerFactory.generateCertificate(ins);
			//创建一个证书库，并将证书导入证书库
			KeyStore keyStore=KeyStore.getInstance("PKCS12", "BC");
			keyStore.load(null, null);
			keyStore.setCertificateEntry("trust", cer);
			ins.close();
			//把咱的证书库作为信任证书库
			SSLSocketFactory socketFactory=new SSLSocketFactory(keyStore);
			Scheme sch=new Scheme("https", socketFactory, 443);
			//完工
			HttpClient mHttpClient=new DefaultHttpClient();
			mHttpClient.getConnectionManager().getSchemeRegistry().register(sch);
		}
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//////////////////////////

	private void test1()
	{
		AssetManager am=getAssets();
		try
		{
			InputStream ins=am.open("keystore.jks");
			//读取证书
			CertificateFactory cerFactory=CertificateFactory.getInstance("X.506");
			Certificate cer=cerFactory.generateCertificate(ins);
			//创建一个证书库，并将证书导入证书库
			KeyStore keyStore=KeyStore.getInstance("PKCS12", "BC");
			keyStore.load(null, null);
			keyStore.setCertificateEntry("trust", cer);
			ins.close();
			//把咱的证书库作为信任证书库
			SSLSocketFactory socketFactory=new SSLSocketFactory(keyStore);
			Scheme sch=new Scheme("https", socketFactory, 443);
			//完工
			HttpClient mHttpClient=new DefaultHttpClient();
			mHttpClient.getConnectionManager().getSchemeRegistry().register(sch);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	//////////////////////////////



	protected void findView()
	{
		btnRequest=(Button)findViewById(R.id.btn_request);
		btnRequest.setOnClickListener(clickListener);
	}
	private OnClickListener clickListener=new OnClickListener()
	{
		
		@Override
		public void onClick(View v) 
		{
			String [] nameArray={"Company","Password"};
			String [] keyArray={"中铁十六局","1000111"};
			SSLConnection.allowAllSSL(MainActivity.this);
			WYDDataRequire require=new WYDDataRequire();
			require.getData("Company", "checkLogin", nameArray, keyArray, dataRequireListener);

			new Thread()
			{
				public void run()
				{
					getHttps();
					testSSL();
				}
			}.start();
		}
	};
	///////////////
	private void testSSL1()
	{
		try {

			// Load CAs from an InputStream
			// (could be from a resource or ByteArrayInputStream or ...)
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			// From https://www.washington.edu/itconnect/security/ca/load-der.crt
			InputStream caInput =MainActivity.this.getResources().getAssets().open("keystore.jks");//load-der.crt");
			Certificate ca;
			try {
				ca = cf.generateCertificate(caInput);
				System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
			} finally {
				caInput.close();
			}

			// Create a KeyStore containing our trusted CAs
			String keyStoreType = KeyStore.getDefaultType();
			KeyStore keyStore = KeyStore.getInstance(keyStoreType);
			keyStore.load(null, null);
			keyStore.setCertificateEntry("ca", ca);

			// Create a TrustManager that trusts the CAs in our KeyStore
			String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
			tmf.init(keyStore);

			// Create an SSLContext that uses our TrustManager
			SSLContext context = SSLContext.getInstance("TLS");
			context.init(null, tmf.getTrustManagers(), null);

			// Tell the URLConnection to use a SocketFactory from our SSLContext
//		URL url = new URL("https://certs.cac.washington.edu/CAtest/");
			URL url = new URL("https://mail.gxecard.com:8440/service");
			HttpsURLConnection urlConnection =
					(HttpsURLConnection)url.openConnection();
			urlConnection.setSSLSocketFactory(context.getSocketFactory());
			BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = br.readLine()) != null)
				sb.append(line);
			System.out.println(sb.toString());
		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	///////////////////

	private void testSSL()
	{
		try {
			
		// Load CAs from an InputStream
		// (could be from a resource or ByteArrayInputStream or ...)
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		// From https://www.washington.edu/itconnect/security/ca/load-der.crt
		InputStream caInput =MainActivity.this.getResources().getAssets().open("load-der.crt");//load-der.crt");
		Certificate ca;
		try {
		    ca = cf.generateCertificate(caInput);
		    System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
		} finally {
		    caInput.close();
		}

		// Create a KeyStore containing our trusted CAs
		String keyStoreType = KeyStore.getDefaultType();
		KeyStore keyStore = KeyStore.getInstance(keyStoreType);
		keyStore.load(null, null);
		keyStore.setCertificateEntry("ca", ca);

		// Create a TrustManager that trusts the CAs in our KeyStore
		String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
		tmf.init(keyStore);

		// Create an SSLContext that uses our TrustManager
		SSLContext context = SSLContext.getInstance("TLS");
		context.init(null, tmf.getTrustManagers(), null);

		// Tell the URLConnection to use a SocketFactory from our SSLContext
//		URL url = new URL("https://certs.cac.washington.edu/CAtest/");
		URL url = new URL("https://192.168.1.111:8443/AijiaWebSvc/");
		HttpsURLConnection urlConnection =
		    (HttpsURLConnection)url.openConnection();
		urlConnection.setSSLSocketFactory(context.getSocketFactory());
		BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));   
        StringBuffer sb = new StringBuffer();   
        String line;   
        while ((line = br.readLine()) != null)   
        sb.append(line);
        System.out.println(sb.toString());
		} 
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	@SuppressLint("TrulyRandom")
	private void getHttps()
	{
		String https="https://192.168.1.110:8443/AijiaWebSvc/";
		try 
		{
//			SSLContext sc = SSLContext.getInstance("TLS");  
//            sc.init(null, new TrustManager[]{new MyTrustManager()}, new SecureRandom());  
//            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());  
//            HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());  
//			SSLConnection.allowAllSSL();
            HttpsURLConnection conn = (HttpsURLConnection)new URL(https).openConnection(); 
            conn.setSSLSocketFactory(TwoWaysAuthenticationSSLSocketFactory.getSSLSocketFactory(MainActivity.this));
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
            conn.connect();  
              
             BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));   
             StringBuffer sb = new StringBuffer();   
             String line;   
             while ((line = br.readLine()) != null)   
             sb.append(line);
             System.out.println(sb.toString());
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	private class MyHostnameVerifier implements HostnameVerifier
	{

		@Override
		public boolean verify(String hostname, SSLSession session) {
			// TODO Auto-generated method stub
			return true;
		}
		
	}
	private class MyTrustManager implements X509TrustManager
	{

		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
			// TODO Auto-generated method stub
			
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			// TODO Auto-generated method stub
//			return null;
			return new X509Certificate[] {}; 
		}
		
	}
	WYDDataRequire.DataRequireListener dataRequireListener=new WYDDataRequire.DataRequireListener() {
		
		@Override
		public void data_RequireProgress(int progress) 
		{
			System.out.println(progress+"");
		}
		
		@Override
		public void data_RequireFinidhed(String str)
		{
			System.out.println(str);
		}
		
		@Override
		public void data_RequireFailed(String error) 
		{
			System.out.println(error);
		}
	};
}
