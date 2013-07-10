package de.team55.mms.function;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {

	/**
	 * Sendet eine e-Mail
	 * 
	 * @param from
	 *            Absender
	 * @param to
	 *            Empfänger
	 * @param mes
	 *            Nachricht
	 * @return status, ob erfolgreich oder nicht
	 */
	public static int send(String from, String to, String mes) {
		// Mail Server speichern
		String host = "mail.uni-ulm.de";
		Properties properties = System.getProperties();
		properties.setProperty("mail.smtp.host", host);

		// Session anlegen
		Session session = Session.getDefaultInstance(properties);

		try {
			// Nachricht erstellen
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			message.setSubject("Freischaltung beim MMS");

			message.setText(mes);

			// Sende Nachricht
			Transport.send(message);
			return 1;
		} catch (MessagingException e) {
			return 0;
		}
	}
}