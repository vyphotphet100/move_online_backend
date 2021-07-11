import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

@SpringBootApplication(scanBasePackages = "com.move_up")
@EnableJpaRepositories("com.move_up.repository")
@EntityScan("com.move_up.entity")
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class Application {
	public static void main(String args[]) {
		SpringApplication.run(Application.class, args);
		//func();
	}

//	static void func() {
//		try {
//
//			URL url = new URL("https://mbasic.facebook.com/vy.caodinh.52/posts/342505827254991");
//
//			// read text returned by server
//			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
//
//			StringBuilder content = new StringBuilder();
//			String line;
//			while ((line = in.readLine()) != null) {
//				//System.out.println(line);
//				content.append(line);
//			}
//			in.close();
//
//			// get facebook link
//			StringBuilder facebookLink = new StringBuilder();
//			int _countSlash = 0;
//			for (int i=content.toString().indexOf("<link rel=\"canonical\" href=\"")+28; _countSlash<4; i++) {
//				if (content.toString().charAt(i) == '/')
//					_countSlash++;
//				facebookLink.append(content.charAt(i));
//			}
//			System.out.println(facebookLink.toString());
//
//			// get facebook name
//			StringBuilder facebookName = new StringBuilder();
//			for (int i=content.toString().indexOf("<title>")+7; true; i++) {
//				if (content.toString().charAt(i) == '-')
//					break;
//				facebookName.append(content.charAt(i));
//			}
//			System.out.println(facebookName.toString());
//
//
//
//		}
//		catch (MalformedURLException e) {
//			System.out.println("Malformed URL: " + e.getMessage());
//		}
//		catch (IOException e) {
//			System.out.println("I/O Error: " + e.getMessage());
//		}
//	}
}
