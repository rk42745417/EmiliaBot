import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.entities.*
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.awt.Color

fun main(args: Array<String>) {
    val jda = JDABuilder.createDefault(args[0])
        .addEventListeners(Commands(), Ready())
        .build()
}

fun warning(message: String, channel: MessageChannel) {
    channel.sendMessage(EmbedBuilder()
            .setTitle("警告")
            .setDescription(message)
            .setColor(Color.RED)
            .build()).queue()
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

        if(message.author.idLong == 303139670508830730L) {
            ownerCommand(command, event, content, message)
        }

        if(command.matches(Regex("[0-9]{6}"))) {
            HentaiConverter(command, event.textChannel, message).proc()
        }
        if(command == "pixiv") {
            if(content.size == 1 || content[1].isEmpty())
                warning("用法錯誤，pixiv! <id>", event.channel)
            else
                PixivConverter(content[1], event.textChannel).proc()
        }
    }
    private fun ownerCommand(command: String, event: MessageReceivedEvent, content: List<String>, message: Message) {
        if(content.size == 1)
            return
        if(command == "avatar") {
            event.jda.retrieveUserById(content[1]).queue { user ->
                if (user != null) {
                    println(user.name)
                    message.channel.sendMessage(EmbedBuilder()
                            .setColor(Color.CYAN)
                            .setTitle("Avatar of")
                            .setDescription("<@!${user.id}>")
                            .setImage(user.avatarUrl)
                            .build()
                    ).queue()
                }
            }
        }
        if(command == "say") {
            message.channel.sendMessage(content.subList(1, content.size).joinToString("")).queue()
            message.delete().queue()
        }
        if(command == "remove") {
            message.channel.history.getMessageById(content[1])?.delete()?.queue()
            message.delete().queue()
        }
        if(command == "removes") {
            try {
                val times = content[1].toInt()
                if(times in 1..10) {
                    message.channel.history.retrievePast(times + 1).queue { messages ->
                        messages.forEach {message ->
                            message.delete().queue()
                        }
                    }
                }
                else {
                    warning("請輸入10以內的正整數", event.channel)
                }
            } catch(e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

class Ready : ListenerAdapter() {
    override fun onReady(event: ReadyEvent) {
        println("Bot is Ready!")
    }
}