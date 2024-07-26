package me.darko.TeamWhisper.commands

import me.darko.TeamWhisper.TeamManager
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player

class TeamCreateCommand(private val config: FileConfiguration) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage(ChatColor.RED.toString() + "[Team Whisper] This command can only be used by players.")
            return true
        }

        if (!sender.hasPermission("teamwhisper.command.teamcreate")) {
            sender.sendMessage(ChatColor.RED.toString() + "[Team Whisper] You do not have permission to use this command.")
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage(ChatColor.RED.toString() + "[Team Whisper] Please specify a team name.")
            return false
        }

        val teamName = args.joinToString(" ")
        val player = sender as Player

        if (TeamManager.getTeamByPlayer(player) != null) {
            sender.sendMessage(ChatColor.RED.toString() + "[Team Whisper] You are already in a team or own one.")
            return true
        }

        if (TeamManager.createTeam(teamName, player)) {
            sender.sendMessage(ChatColor.GREEN.toString() + "[Team Whisper] Team '$teamName' created successfully!")
        }

        return true
    }
}
