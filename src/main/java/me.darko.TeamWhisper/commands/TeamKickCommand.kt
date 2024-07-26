package me.darko.TeamWhisper.commands

import me.darko.TeamWhisper.TeamManager
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import org.bukkit.ChatColor

class TeamKickCommand(private val config: FileConfiguration) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (sender !is Player) {
            sender.sendMessage("[Team Whisper] This command can only be used by players.")
            return true
        }

        if (!sender.hasPermission("teamwhisper.command.teamkick")) {
            sender.sendMessage(ChatColor.RED.toString() + "[Team Whisper] You do not have permission to use this command.")
            return true
        }

        if (!sender.hasPermission("teamwhisper.command.teamkick")) {
            sender.sendMessage(ChatColor.RED.toString() + "[Team Whisper] You do not have permission to use this command.")
            return true
        }

        if (args.isEmpty()) {
            sender.sendMessage("[Team Whisper] Please specify a player to kick.")
            return false
        }

        val team = TeamManager.getTeamByPlayer(sender)
        if (team == null || !team.isOwner(sender)) {
            sender.sendMessage("[Team Whisper] You are not the owner of a team.")
            return true
        }

        val playerName = args[0]
        val kickedPlayer = Bukkit.getPlayer(playerName)
        if (kickedPlayer == null || !team.isMember(kickedPlayer)) {
            sender.sendMessage("[Team Whisper] Player not found or not a member of your team.")
            return true
        }

        team.removeMember(kickedPlayer)
        sender.sendMessage("[Team Whisper] Player $playerName kicked from the team.")
        kickedPlayer.sendMessage("[Team Whisper] You have been kicked from the team ${team.name}.")
        return true
    }
}
