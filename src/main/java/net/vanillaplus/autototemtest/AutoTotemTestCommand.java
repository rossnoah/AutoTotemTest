package net.vanillaplus.autototemtest;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
                        if(target.getInventory().getItemInOffHand().getType()== Material.TOTEM_OF_UNDYING){
                            int count = 0;
                            for (ItemStack i:target.getInventory().getContents()) {
                                if(i!=null&&i.getType()==Material.TOTEM_OF_UNDYING){
                                   count++;
                                }

                            }
                            if(count>1){

                              //  targetInventory().getItemInOffHand().setType(Material.AIR);rget.
                                Block b = target.getLocation().getBlock();
                                    if(b.isEmpty()) {
                                        float speed = target.getWalkSpeed();
                                        final boolean[] isCheating = {false};

                                        //target.damage();

                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                target.setWalkSpeed(0);
                                                b.setType(Material.NETHER_PORTAL);
                                                target.damage(target.getHealth());
                                                sender.sendMessage("§aTotem popped!");
                                            }
                                        }.runTaskLater(plugin, 1L);


                                        BukkitTask noMove = new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                target.teleport(b.getLocation().add(0.5,0,0.5));
                                            }
                                        }.runTaskTimer(plugin, 1L /*<-- the initial delay */, 1L /*<-- the interval */);

                                        BukkitTask totemCheck = new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                if (target.getInventory().getItemInOffHand().getType() == Material.TOTEM_OF_UNDYING) {
                                                isCheating[0] =true;
                                                }

                                            }
                                        }.runTaskTimer(plugin, 2L /*<-- the initial delay */, 1L /*<-- the interval */);


                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                noMove.cancel();
                                                totemCheck.cancel();

                                                b.setType(Material.AIR);
                                                target.setWalkSpeed(speed);
                                                if (isCheating[0]) {
                                                    sender.sendMessage("§cAutototem test failed! §4" + target.getName() + "§4 is cheating");
                                                }else{
                                                    sender.sendMessage("§aAutototem test passed! §2" + target.getName() + "§2 is not cheating");

                                                }



                                            }
                                        }.runTaskLater(plugin, 20L * 3L /*<-- the delay */);


                                        //target.getLocation().getBlock().setType(Material.AIR);


                                }else{
                                    sender.sendMessage("§4"+target.getName()+" is not standing in an empty block");
                                }

                            }else{
                                sender.sendMessage("§4"+target.getName()+" does not any extra totems");

                            }

                        }else{
                            sender.sendMessage("§4"+target.getName()+" does not have a totem in their offhand");
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
