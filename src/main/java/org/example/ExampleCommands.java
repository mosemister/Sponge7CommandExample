package org.example;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.EntityType;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

import java.util.Arrays;
import java.util.Optional;


public class ExampleCommands {

    //defines the keys of the arguments -> these are needed so the executor can find the arguments for the command
    private static final Text KEY_X = Text.of("x");
    private static final Text KEY_Y = Text.of("y");
    private static final Text KEY_Z = Text.of("z");
    private static final Text KEY_WORLD = Text.of("world");

    public static class SpawnItem implements CommandExecutor {

        @Override
        public CommandResult execute(CommandSource src, CommandContext args) {
            //gets the arguments
            Optional<Double> opX = args.getOne(KEY_X);
            Optional<Double> opY = args.getOne(KEY_Y);
            Optional<Double> opZ = args.getOne(KEY_Z);
            Optional<WorldProperties> opWorldProperties = args.getOne(KEY_WORLD);

            //checks that the arguments were actually entered  -> while this is done by sponge automatically, you shouldn't rely on it
            if(!opX.isPresent()){
                //tells sponge that the command went wrong
                return CommandResult.empty();
            }
            if(!opY.isPresent()){
                return CommandResult.empty();
            }
            if(!opZ.isPresent()){
                return CommandResult.empty();
            }
            if(!opWorldProperties.isPresent()){
                return CommandResult.empty();
            }


            //the world argument gets WorldProperties (The properties of a world ... loaded or not), therefore we need to convert it to a loaded world -> no point spawning an item in a unloaded world
            Optional<World> opWorld = Sponge
                    .getServer()
                    .getWorld(opWorldProperties
                            .get()
                            .getUniqueId());


            if(!opWorld.isPresent()){
                return CommandResult.empty();
            }

            //gets the spawn location
            Location<World> location = opWorld.get().getLocation(
                    opX.get(),
                    opY.get(),
                    opZ.get()
            );

            //builds the item into memory
            ItemStack stack = ItemStack
                    .builder()
                    .itemType(ItemTypes.GOLDEN_SWORD)
                    .add(Keys.ITEM_LORE, Arrays.asList(
                            Text.of("This is a "),
                            Text.of("example item")))
                    .add(Keys.DISPLAY_NAME, Text.of("Mose's Sword"))
                    .build();

            //creates the item entity
            Item item = (Item)location.createEntity(EntityTypes.ITEM);

            //sets the item of the item entity
            item.offer(Keys.REPRESENTED_ITEM, stack.createSnapshot());

            //spawns the item into the world
            opWorld.get().spawnEntity(item);

            //tells sponge that the command was successful
            return CommandResult.success();
        }
    }

    public static CommandSpec registerCommands(){
        //I personally like to split all the child commands into variables and then combine them at the end
        CommandSpec spawnItemCommand = CommandSpec
                .builder()
                .arguments(
                        //adds the arguments of the command
                        GenericArguments.doubleNum(KEY_X),
                        GenericArguments.doubleNum(KEY_Y),
                        GenericArguments.doubleNum(KEY_Z),
                        GenericArguments.world(KEY_WORLD)
                )
                .executor(new SpawnItem())
                .build();

        //adds the child command of spawn to this command, meaning that the whole command would be
        // example spawn <x> <y> <z> <world>
        return CommandSpec
                .builder()
                .child(spawnItemCommand, "spawn")
                .build();
    }
}





















