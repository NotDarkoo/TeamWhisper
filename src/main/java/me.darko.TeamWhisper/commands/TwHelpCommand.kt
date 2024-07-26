package me.darko.TeamWhisper.commands

import me.darko.TeamWhisper.TeamManager
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player

class TwHelpCommand(private val config: FileConfiguration) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.hasPermission("teamwhisper.command.twhelp")) {
            sender.sendMessage(ChatColor.RED.toString() + "[Team Whisper] You do not have permission to use this command.")
            return true
        }

        val helpMessage = """
            ${ChatColor.GREEN}[Team Whisper] Help:
            ${ChatColor.GOLD}/twreload${ChatColor.WHITE} - Reload the config file.
            ${ChatColor.GOLD}/twhelp${ChatColor.WHITE} - Show this help message.
            ${ChatColor.GOLD}/teamwhisper${ChatColor.WHITE} - Show this help message.
            ${ChatColor.GOLD}/teamchat <message>${ChatColor.WHITE} - Send a message to your team.
            ${ChatColor.GOLD}/teamcreate <teamName>${ChatColor.WHITE} - Create a new team.
            ${ChatColor.GOLD}/teamdisband <teamName>${ChatColor.WHITE} - Disband an existing team.
            ${ChatColor.GOLD}/teaminvite <playerName>${ChatColor.WHITE} - Invite a player to your team.
            ${ChatColor.GOLD}/teamaccept${ChatColor.WHITE} - Accept a team invitation.
            ${ChatColor.GOLD}/teamkick${ChatColor.WHITE} - Kick a player from your team.
            ${ChatColor.GOLD}/teamleave${ChatColor.WHITE} - Leave from your current team.
            ${ChatColor.GOLD}/teaminfo${ChatColor.WHITE} - Show info about your team.

        """.trimIndent()

        sender.sendMessage(helpMessage)
        return true
    }
}
