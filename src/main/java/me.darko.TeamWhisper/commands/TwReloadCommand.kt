package me.darko.TeamWhisper.commands

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class TwReloadCommand(private val plugin: JavaPlugin) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.hasPermission("teamwhisper.command.reload")) {
            sender.sendMessage(ChatColor.RED.toString() + "[Team Whisper] You do not have permission to use this command.")
            return true
        }

        sender.sendMessage("[Team Whisper] Reloading configuration and data...")

        try {
            plugin.reloadConfig()
            sender.sendMessage("[Team Whisper] Configuration reloaded.")
        } catch (e: Exception) {
            sender.sendMessage(ChatColor.RED.toString() + "[Team Whisper] Failed to reload configuration: ${e.message}")
            return true
        }

        return true
    }
}
