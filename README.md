# Hold Your Breath - A Minecraft Mod

A Minecraft mod for Fabric 1.20.1 which makes players hold their breath in real life when they go underwater.

## Breath Holding

Players press the `R` key (configurable) to tell the server that they are holding their breath.
When a player stops holding their breath, they will release the `R` key.
This requires the honor system to work.

A player will need to press `R` before entering the water to avoid drowning.
As long as the `R` key is held down, players will not lose air.
As soon as the `R` key is released, players will start losing air at a drastically increased rate
(this rate is configurable).
This way, players can stay underwater as long as they can hold their breath, but once they breathe they will start
losing air.

If the `R` key is released underwater, pressing it again will not stop air loss.
You can think of this as exhaling underwater causing your mouth to fill up with water, preventing breathing.

There is an option in the config to make the `R` key into a toggle rather than something you have to hold down.
This option can be set separately by each player.
This does make it easier for players to cheat, but it doesn't allow them to do anything that they couldn't have already
accomplished with a program like AutoHotKey.
Since the mod already operates on an honor system, this isn't that big of a deal.
There are several legitimate reasons to use this feature, such as a slow connection, but please ask a server admin
before setting this option to "true" on a server.

## Miscellaneous Features

As an option in the config, you can prevent doors from being placed underwater to make air pockets.
This is disabled by default.

## License

Hold Your Breath is licensed under the MIT License. This means you can do mostly whatever you would like with this mod.

This mod includes a copy of MidnightLib in its jar, which is also licensed under the MIT License.
