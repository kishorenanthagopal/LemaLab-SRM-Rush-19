package com.example.lemalabsample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

public class MainActivity13 extends AppCompatActivity implements View.OnClickListener {


    WebView simpleWebView;
    Button loadFromStaticHtml;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main13);
        loadFromStaticHtml = (Button) findViewById(R.id.loadFromStaticHtml);
        loadFromStaticHtml.setOnClickListener(this);
        simpleWebView = (WebView) findViewById(R.id.simpleWebView);

    }

    @Override
    public void onClick(View v) {

        String customHtml = "<html>\n" +
                "<head>\n" +
                "<title>welcome</title>\n" +
                "</head>\n" +
                "<body bgcolor=\"black\"><center>\n" +
                "<h1><font color=\"#006400\"><b>INTERNET OF THINGS</b></font></h1>\n" +
                "<h2><font color=\"white\">Make a web-based Raspberry Pi home automation system using normal HTTP protocols.</h2></center><br>\n" +
                "<p align=\"justify\"><font face= \"Times New Roman\" size=\"5\" color=\"white\">\t&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;The Internet of Things is the most trending technology today, alongside wearables and robotics. The concept is simple: Devices in your home (or wherever they are) have the capability to communicate with each other via the internet. This technology usually uses sensors to pass data to the internet. Imagine a sensor installed in your garden that uploads data like temperature, humidity, and soil purity to the internet, and this data is visible to you from anywhere in the world. Or imagine home automation systems you can use to control appliances in your home like lights, door locks, and air conditioning through a web interface or smartphone application. A lot of technologies are being developed around this concept, such as independent lightweight IoT networks and protocols for passing data. Here, I will show you how to make a web-based Raspberry Pi home automation system that will allow you to control any appliance in your home from anywhere in the world using normal HTTP protocols.</font></p>\n" +
                "<h2>\n" +
                "<b><br>\n" +
                "<i><font color=\"#006400\" face=\"arial\">Step 1: Preparing the HTML and PHP Files </font color><br><br></i></b></h2>\n" +
                "<p align=\"justify\"><font face= \"Times New Roman\" size=\"5\" color=\"white\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;First, the server side of the system has to be set up. If you have your own domain, you can use the file manager service to drop these files into your server and control your IoT systems from anywhere in the world. The files to be put on your server are main.html, button.php, buttonStatus.php, and buttonStatus.txt. Download them from the tutorial and extract it. The program raspbi.py is the Python program, which is to be copied to your Raspberry Pi. If you are new to HTML and PHP, don't worry. This project will need only a basic understanding of the two languages, spending some time on a few tutorials and reading the code would be sufficient. The HTML file consists of a basic UI with two buttons. On button press, it triggers a PHP program that writes a string to a .txt file depending on the button pressed. The .txt file stores information of the last button state.</font> </p>\n" +
                "<h2>\n" +
                "<b><br>\n" +
                "<i><font color=\"#006400\" face=\"arial\">step 2: Setting Up the Relay Circuit for the Raspberry Pi Home Automation System </font color></br><br></i></b></h2>\n" +
                "<center>\n" +
                "<img src=\"iiii.jpg\">\n" +
                "<p><font size=\"2\">Relay Circuit for the Raspberry Pi home automation system</font></p></center>\n" +
                "<p align=\"justify\"><font face= \"Times New Roman\" size=\"5\" color=\"white\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;  A small relay circuit is to be made, to switch an appliance ON/OFF. It consists of a BC547 transistor, which acts as a switch. The transistor triggers the relay at its base from the GPIO pin of Raspberry Pi. A 1N4001 diode is used to protect the transistor from reverse voltages created in the relay coil. The circuit can be powered either using the 5V supply from the Raspberry Pi or using an external battery. Use the schematic and pinout diagram below to make the connections for the Raspberry Pi home automation system.</p>\n" +
                "\n" +
                "\n" +
                "\n" +
                "</body>\n" +
                "</html>";
        simpleWebView.loadData(customHtml, "text/html", "UTF-8");
    }


}

