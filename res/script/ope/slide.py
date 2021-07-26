#this is slide lib for OPE. Created by rafi612
from enum import Enum

class Animation(Enum):
    APPEARING = "appearing"
    DISAPPEARANCE = "disappearance"
    NONE = "none"

projectpath = "";
slides = 0;

fullscreen = True
music = "null"
ttskey = "auto"

slidespath = []
slideslayout = []
slidesbg = []
slidestts = []
slidesentani  = []
slidesexiani  = []

num = 0;

def saveFileWriteLine(name,text):
    f = open(name,"a+")
    f.write(text);
    f.close()

def saveFile(name,text):
    f = open(name,"w+")
    f.write(text);
    f.close();

def setFullscreen(arg0):
    global fullscreen
    fullscreen = arg0
        
def setGeneralMusic(m):
    global music
    music = m
    
def setTTSKey(k):
    global ttskey
    ttskey = k

def setCurrentSlide(s):
    global num
    num = s
    
def createSlide(number,path):
    global slides
    global num
    num = number
    slides = slides + 1
    slidespath.append(path)
    slidesbg.append("null")
    slidestts.append("null")
    slidesentani.append("null")
    slidesexiani.append("null")

def setLayout(path):
    global slideslayout
    global num
    #del slideslayout[num - 1]
    slideslayout.insert(num - 1,path)

def setSlideBg(path):
    global slidesbg
    global num
    del slidesbg[num - 1]
    slidesbg.insert(num - 1,path)
    
def setSlideTTS(path):
    global slidestts
    global num
    del slidestts[num - 1]
    slidestts.insert(num - 1,path)
    
def setEntranceAnimation(path):
    global slidesentani
    global num
    del slidesentani[num - 1]
    slidesentani.insert(num - 1,path.value)

def setExitAnimation(path):
    global slidesexiani
    global num
    del slidesexiani[num - 1]
    slidesexiani.insert(num - 1,path.value)
  
def End():
    f = open("config.xml","w+")
    f.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?> \n")
    f.close()
    saveFileWriteLine("config.xml","<class> \n")
    for i in range(slides):
        saveFileWriteLine("config.xml","<slide" + str(i + 1) + ">\n")
        saveFileWriteLine("config.xml","    <layout>" + slideslayout[i] + "</layout> \n");
        saveFileWriteLine("config.xml","    <path>" + slidespath[i] + "</path> \n");
        saveFileWriteLine("config.xml","    <bg>" + slidesbg[i] + "</bg> \n")
        saveFileWriteLine("config.xml","    <tts>" + slidestts[i] + "</tts> \n")
        saveFileWriteLine("config.xml","    <ent_ani>" + slidesentani[i] + "</ent_ani> \n")
        saveFileWriteLine("config.xml","    <exi_ani>" + slidesexiani[i] + "</exi_ani> \n")
        saveFileWriteLine("config.xml","</slide" + str(i + 1) + "> \n")
    saveFileWriteLine("config.xml","<summary> \n");
    saveFileWriteLine("config.xml","    <slides>" + str(slides) + "</slides> \n")
    saveFileWriteLine("config.xml","    <fullscreen>" + str(fullscreen) + "</fullscreen> \n")
    saveFileWriteLine("config.xml","    <general_music>" + music + "</general_music> \n")
    saveFileWriteLine("config.xml","    <ttskey>" + ttskey + "</ttskey> \n")
    saveFileWriteLine("config.xml","</summary> \n")
    saveFileWriteLine("config.xml","</class>")
    
