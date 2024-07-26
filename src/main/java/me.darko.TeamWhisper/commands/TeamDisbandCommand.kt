package me.darko.TeamWhisper.commands

import me.darko.TeamWhisper.TeamManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.ChatColor
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player

class TeamDisbandCommand(private val config: FileConfiguration) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("[Team Whisper] This command can only be used by players.")
            return true
        }

        if (!sender.hasPermission("teamwhisper.command.teamdisband")) {
            sender.sendMessage(ChatColor.RED.toString() + "[Team Whisper] You do not have permission to use this command.")
            return true
        }

        val team = TeamManager.getTeamByPlayer(sender)
        if (team == null || !team.isOwner(sender)) {
            sender.sendMessage("[Team Whisper] You are not the owner of a team.")
            return true
        }

        TeamManager.disbandTeam(team.name)
        sender.sendMessage("[Team Whisper] Team ${team.name} disbanded successfully.")
        return true
    }
}
