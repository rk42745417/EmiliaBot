import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.GuildChannel
import net.dv8tion.jda.api.entities.PrivateChannel
import net.dv8tion.jda.api.entities.SelfUser
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException
import net.dv8tion.jda.api.hooks.ListenerAdapter

fun main(args: Array<String>) {
    val builder = JDABuilder.createDefault(args[0])
        .addEventListeners(Commands(), Ready())
        .build()
}

class Commands : ListenerAdapter() {
    override fun onMessageReceived(event : MessageReceivedEvent) {
        if(event.author.isBot)
            return
        if(event.channel is PrivateChannel) {
            event.privateChannel.sendMessage("私我幹三小").queue()
        }

        val message = event.message
        val content = message.contentRaw.split(' ')

        var command = content.getOrNull(0)
        if(command?.getOrNull(command.length - 1) != '!')
            return
        command = command.substring(0, command.length - 1)

        if(command.matches(Regex("[0-9]{6}"))) {
            HentaiConverter(command, event.textChannel, message).proc()
        }
        if(command == "pixiv" && content.size > 1 && content[1].isNotEmpty()) {
            PixivConverter(content[1], event.textChannel).proc()
        }
    }
}

class Ready : ListenerAdapter() {
    override fun onReady(event: ReadyEvent) {
        println("Bot is Ready!")
    }
}