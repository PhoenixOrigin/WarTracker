# WarTracker
A very simple mod that allows you to track wars and post to an API

# How to use
- Host a POST api that takes this data and stores it locally (will be making one soon)
- modify the config (.minecraft/config/phoenix/wartracker.yml) with the content
-     url=YourAPIUrlHere
- Put the mod in your mod folder
- Wars should be tracked now

# Hosting your own API
The mod will send the data to the url in config + /war. For example, if the config looks like this:
```
url=http://localhost:8080
```
then, the war will be sent to http://localhost:8080/war. Data will be sent in the following format
```
{
  "warMembersUUID": ["Array<String>"],
  "territoryName": "String",
  "territoryOwner": "String",
  "start": {
    "health": "Int",
    "attackLow": "Int",
    "attackHigh": "Int",
    "defense": "Float",
    "atckSpeed": "Float"
  },
  "end": {
    "health": "Int",
    "attackLow": "Int",
    "attackHigh": "Int",
    "defense": "Float",
    "atckSpeed": "Float"
  },
  "startTime": "Epoch Mili Long",
  "killed": "boolean"
}
```
with the headers
```
X-Minecraft-Username: String
X-Minecraft-UUID: String
```
You can do with that data as you will
