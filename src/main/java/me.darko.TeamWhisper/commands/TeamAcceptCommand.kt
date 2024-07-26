package me.darko.TeamWhisper.commands

import me.darko.TeamWhisper.TeamManager
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player

class TeamAcceptCommand(private val config: FileConfiguration) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("[Team Whisper] This command can only be used by players.")
            return true
        }

        if (!sender.hasPermission("teamwhisper.command.teamaccept")) {
            sender.sendMessage(ChatColor.RED.toString() + "[Team Whisper] You do not have permission to use this command.")
            return true
        }

        val currentTeam = TeamManager.getTeamByPlayer(sender)
        if (currentTeam != null) {
            sender.sendMessage("[Team Whisper] You are already in a team or own one. Leave your current team before accepting a new invitation.")
            return true
        }

        if (TeamManager.acceptInvite(sender)) {
            sender.sendMessage("[Team Whisper] You have joined the team.")
        } else {
            sender.sendMessage("[Team Whisper] You have not been invited to any team.")
        }
        return true
    }
}
