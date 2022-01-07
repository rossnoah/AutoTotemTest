package net.vanillaplus.autototemtest;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class AutoTotemTestCommand implements CommandExecutor {

    private final Main plugin = Main.getPlugin(Main.class);



    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            Player sender = (Player) commandSender;
            if (strings.length!=1){
                sender.sendMessage("§4Incorrect Usage. §cUse /totemtest <player>");
            }else{
                if(Bukkit.getPlayer(strings[0])!=null){
                    Player target = Bukkit.getPlayer(strings[0]);
                    if (target != null) {
                        if(target.getGameMode()== GameMode.SURVIVAL||target.getGameMode()==GameMode.ADVENTURE){
                                if(!target.isInvulnerable()&&!target.isInvisible()) {
                                    if (target.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING) {
                                        boolean hasExtraTotems = false;

                                        for(int i=9;i<35;i++){
                                            if (target.getInventory().getItem(i) != null &&
                                                    target.getInventory().getItem(i).getType() == Material.TOTEM_OF_UNDYING) {
                                                hasExtraTotems = true;
                                            }
                                        }

                                        if (hasExtraTotems) {

                                            ItemStack item;
                                            boolean[] hotbar = new boolean[9];

                                            for(int i=0;i<9;i++){
                                            item= target.getInventory().getItem(i);
                                            if (item != null && item.getType() == Material.TOTEM_OF_UNDYING) {
                                                hotbar[i]=true;
                                                target.getInventory().setItem(i,null);
                                            }
                                        }

                                            //  targetInventory().getItemInOffHand().setType(Material.AIR);rget.
                                            Block b = target.getLocation().getBlock();
                                            if (b.isEmpty()) {
                                                final boolean[] isCheating = {false};


                                                        b.setType(Material.NETHER_PORTAL);
                                                        target.damage(target.getHealth());
                                                        sender.sendMessage("§aTotem popped!");


                                                BukkitTask noMove = new BukkitRunnable() {
                                                    @Override
                                                    public void run() {
                                                        target.teleport(b.getLocation().add(0.5, 0, 0.5));
                                                    }
                                                }.runTaskTimer(plugin, 0 /*<-- the initial delay */, 1L /*<-- the interval */);

                                                BukkitTask totemCheck = new BukkitRunnable() {
                                                    @Override
                                                    public void run() {
                                                        if (target.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING) {
                                                            isCheating[0] = true;
                                                        }

                                                    }
                                                }.runTaskTimer(plugin, 1L /*<-- the initial delay */, 1L /*<-- the interval */);


                                                new BukkitRunnable() {
                                                    @Override
                                                    public void run() {
                                                        noMove.cancel();
                                                        totemCheck.cancel();
                                                        ItemStack totem = new ItemStack(Material.TOTEM_OF_UNDYING);
                                                        for(int i = 0;i<9;i++){
                                                            if(hotbar[i]){
                                                                target.getInventory().setItem(i,totem);
                                                            }
                                                        }

                                                        b.setType(Material.AIR);
                                                        if (isCheating[0]) {
                                                            sender.sendMessage("§cAutototem test failed! §4" + target.getName() + "§4 is cheating");
                                                        } else {
                                                            sender.sendMessage("§aAutototem test passed! §2" + target.getName() + "§2 is not cheating");
                                                            if(target.getInventory().getItem(EquipmentSlot.OFF_HAND)==null||target.getInventory().getItem(EquipmentSlot.OFF_HAND).getType()==Material.AIR) {
                                                                target.getInventory().setItem(EquipmentSlot.OFF_HAND, totem);
                                                                sender.sendMessage("§2Totem returned to player");
                                                            }else{
                                                                sender.sendMessage("§4Totem NOT returned to player");

                                                            }
                                                        }



                                                    }
                                                }.runTaskLater(plugin, 20L * 3L /*<-- the delay */);


                                                //target.getLocation().getBlock().setType(Material.AIR);


                                            } else {
                                                sender.sendMessage("§4" + target.getName() + " is not standing in an empty block");
                                            }

                                        } else {
                                            sender.sendMessage("§4" + target.getName() + " does not any extra totems (hotbar totems dont count)");

                                        }

                                    } else {
                                        sender.sendMessage("§4" + target.getName() + " does not have a totem in their offhand");
                                    }

                                }else{
                                    sender.sendMessage("§4" + target.getName() + "is invulnerable or invisible");
                                }
                        }else{
                            sender.sendMessage("§4" + target.getName() + "is not in survival or adventure mode");

                        }
                    }else{
                        sender.sendMessage("§4Player not found");
                    }
                }else{
                    sender.sendMessage("§4Player not found");
                }
            }

        }else{
            commandSender.sendMessage("§cOnly players can use this command");
        }



        return true;
    }

}
