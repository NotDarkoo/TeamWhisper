package me.darko.TeamWhisper.commands

import me.darko.TeamWhisper.TeamManager
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.ChatColor
import org.bukkit.configuration.file.FileConfiguration

class TeamLeaveCommand(private val config: FileConfiguration) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("[Team Whisper] This command can only be used by players.")
            return true
        }

        if (!sender.hasPermission("teamwhisper.command.teamleave")) {
            sender.sendMessage(ChatColor.RED.toString() + "[Team Whisper] You do not have permission to use this command.")
            return true
        }

        val player = sender as Player
        val team = TeamManager.getTeamByPlayer(player)

        if (team == null) {
            sender.sendMessage("[Team Whisper] You are not a member of any team.")
            return true
        }

        if (team.isOwner(player)) {
            sender.sendMessage("[Team Whisper] You cannot leave the team because you are the owner. Use /team disband to disband the team.")
            return true
        }

        team.removeMember(player)
        sender.sendMessage("[Team Whisper] You have left the team ${team.name}.")

        return true
    }
}
