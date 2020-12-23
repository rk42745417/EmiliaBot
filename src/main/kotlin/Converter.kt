import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.TextChannel

abstract class CodeConverter(val content : String, val channel : TextChannel) {
    abstract fun proc() : CodeConverter
}
class HentaiConverter(content : String, channel : TextChannel, private val message : Message) : CodeConverter(content, channel) {
    override fun proc() : HentaiConverter {
        var canUse = true
        when(content) {
            "228922" -> canUse = false
        }
        try {
            if (canUse) {
                channel.sendMessage("https://nhentai.net/g/$content").queue()
            } else {
                message.addReaction("\uD83D\uDE21").queue()
                channel.sendMessage("不要發靈車").queue()
            }
        } catch(e : Exception) {
            e.printStackTrace()
        }
        return this
    }
}
class PixivConverter(content : String, channel : TextChannel) : CodeConverter(content, channel) {
    override fun proc() : PixivConverter {
        try {
            channel.sendMessage("https://www.pixiv.net/artworks/$content").queue()
        } catch(e : Exception) {
            e.printStackTrace()
        }
        return this
    }
}