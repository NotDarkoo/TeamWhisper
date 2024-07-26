package me.darko.TeamWhisper.commands

import me.darko.TeamWhisper.TeamManager
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player

class TeamInfoCommand(private val config: FileConfiguration) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("[Team Whisper] This command can only be used by players.")
            return true
        }

        if (!sender.hasPermission("teamwhisper.command.teaminfo")) {
            sender.sendMessage(ChatColor.RED.toString() + "[Team Whisper] You do not have permission to use this command.")
            return true
        }

        val team = TeamManager.getTeamByPlayer(sender)
        if (team == null) {
            sender.sendMessage("[Team Whisper] You are not a member of any team.")
            return true
        }

        val owner = team.owner.name
        val members = team.members.mapNotNull { Bukkit.getPlayer(it)?.name } // Get member names
        val membersList = members.joinToString(", ")

        sender.sendMessage("[Team Whisper] ${ChatColor.GREEN}Team Info:")
        sender.sendMessage("${ChatColor.BLUE}Team Name: ${ChatColor.RESET}${team.name}")
        sender.sendMessage("${ChatColor.BLUE}Team Owner: ${ChatColor.RESET}$owner")
        sender.sendMessage("${ChatColor.BLUE}Members: ${ChatColor.RESET}$membersList")

        return true
    }
}
