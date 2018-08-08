package de.geosearchef;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.HashSet;
import java.util.Set;

public class UIDBot {

	private static Logger logger = LoggerFactory.getLogger(UIDBot.class);

	private static Set<String> notifiedUsers = new HashSet<String>();

	public static void main(String args[]) {
		JDABuilder builder = new JDABuilder(AccountType.BOT);
		builder.setToken("NDc2NjU1OTU2NjIxNzg3MTM5.Dkwwdg._WCu2gtdYu-878QEfRSb6M3CenE");
		builder.addEventListener(new Listener());

		logger.info("Starting bot...");
		try {
			builder.build();
		} catch(LoginException e) {
			logger.error("Could not create bot.", e);
			System.exit(1);
		}

		logger.info("Started.");
	}


	private static class Listener extends ListenerAdapter {
		@Override
		public void onMessageReceived(MessageReceivedEvent event) {
			if(event.getAuthor().isBot() || !event.getChannel().getName().equals("technical-help")) {
				return;
			}

			String messageIn = event.getMessage().getContentRaw().toLowerCase();

			if(messageIn.equalsIgnoreCase("!uid")) {
				event.getChannel().sendMessage(String.format(uidMessage, "")).queue();
				return;
			}

			if(notifiedUsers.contains(event.getAuthor().getName())) {
				return;
			}

			if(messageIn.contains("cannot run program \"lib/faf-uid.exe\"")
					|| (messageIn.contains("uid") && messageIn.contains("calculate"))
					|| (messageIn.contains("uid") && messageIn.contains("smurf"))
					|| (messageIn.contains("unique id") && messageIn.contains("calculate"))
					|| (messageIn.contains("unique id") && messageIn.contains("smurf"))) {

				event.getChannel().sendMessage(String.format(uidMessage, "<@" + event.getAuthor().getId() + ">")).queue();
				notifiedUsers.add(event.getAuthor().getName());
				return;
			}

//			System.out.printf("Message from %s in %s: %s", event.getAuthor().getName(), event.getChannel().getName(), event.getMessage());
		}
	}

	private static String uidMessage = "%s You seem to have posted an issue about our UID system.\n" +
			"It is very likely this happens due to you using McAfee.\n" +
			"Please make sure your antivirus didn't delete the faf-uid.exe file.\n" +
			"If this has happened, change your antivirus software or add an exception and restore the file. The file can also be restored by installing the client again.";
}
