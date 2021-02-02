package de.tubeof.tubetils.mail;

import de.tubeof.tubetils.data.Data;
import de.tubeof.tubetils.mail.utils.MailSendException;
import de.tubeof.tubetils.main.TubeTils;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Properties;

@SuppressWarnings("ALL")
public class MailSender {

    private final Data data = TubeTils.getData();
    private final ConsoleCommandSender ccs = Bukkit.getConsoleSender();
    private String mailSenderName;

    private Boolean loggedIn;
    private String smtpHost;
    private String smtpPort;
    private String smtpUsername;
    private String smtpPassword;
    private Boolean starttls;

    private Session session;

    public MailSender(String mailSenderName, String smtpHost, String smtpPort, String smtpUsername, String smtpPassword, Boolean useStarttls, Boolean autoLogin) {
        if(data.isDebuggingEnabled()) ccs.sendMessage(TubeTils.getData().getPrefix() + "Created new MailSender with name: " + mailSenderName);

        this.mailSenderName = mailSenderName;
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.smtpUsername = smtpUsername;
        this.smtpPassword = smtpPassword;
        this.starttls = useStarttls;
    }

    /**
     * Establishes the connection to the SMTP server and logs in with the previously specified data.
     * @return Returns the value true if the login was successful.
     */
    public boolean smtpLogin() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", smtpPort);
        props.put("mail.smtp.socketFactory.port", smtpPort);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        if(starttls) props.put("mail.smtp.starttls.enable", "true");

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(smtpUsername, smtpPassword);
            }
        };
        session = Session.getDefaultInstance(props, auth);

        return true;
    }

    /**
     *
     * @param senderMail
     * @param senderName
     * @param receiverMail
     * @param subject
     * @param message
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     * @throws MailSendException
     */
    public void sendHtmlMail(String senderMail, String senderName, String receiverMail, String subject, String message) throws MessagingException, UnsupportedEncodingException, MailSendException {
        if (session == null) throw new MailSendException("No valid session could be found!");

        MimeMessage msg = new MimeMessage(session);
        msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
        msg.addHeader("format", "flowed");
        msg.addHeader("Content-Transfer-Encoding", "8bit");

        msg.setFrom(new InternetAddress(senderMail, senderName));
        msg.setReplyTo(InternetAddress.parse(senderMail, false));
        msg.setSubject(subject, "UTF-8");
        msg.setContent(message, "text/html");
        msg.setSentDate(new Date());

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverMail, false));

        Transport.send(msg);
    }

}
