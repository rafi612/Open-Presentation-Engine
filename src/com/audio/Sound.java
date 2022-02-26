package com.audio;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;
import static org.lwjgl.openal.EXTThreadLocalContext.alcSetThreadContext;
import static org.lwjgl.system.MemoryUtil.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.system.MemoryUtil;

import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.DecoderException;
import com.audio.AudioDecoder.SoundInfo;
import com.io.Util;

public class Sound 
{	
	static long device,context;
	static ALCapabilities caps;
	
	private boolean isPlay = false;
	
	public SoundInfo info;
	
	InputStream input;
	
	int buffer,source;
	
	Type type;
	
	float gain = 1,pitch = 1,pan = 0;
	
	public enum Type
	{
		MPEG,WAVE,VORBIS
	}
	
	public Sound(String path) throws FileNotFoundException
	{
		Type t = null;
		switch (Util.FileExtension(new File(path)))
		{
		case "mp3":
			t = Type.MPEG;
			break;
		case "wav":
			t = Type.WAVE;
			break;
		case "ogg":
			t = Type.VORBIS;
			break;
		}

		try 
		{
			load(new FileInputStream(path),t);
		} 
		catch (BitstreamException | DecoderException | IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void load(InputStream in,Type type) throws BitstreamException, DecoderException, IOException
	{
		this.input = in;
		
        buffer = alGenBuffers();
        source = alGenSources();
        
        ShortBuffer pcm = null;
        
        info = new SoundInfo();
        
        this.type = type;
        
        switch (type)
        {
        case MPEG:
        	pcm = AudioDecoder.decodeMPEG(in,info);
        	break;
        case VORBIS:
        	pcm = AudioDecoder.decodeVorbis(in, info);
        	break;
		default:
			throw new IOException("Unsupported format");
        }

        
        alBufferData(buffer, info.channels == 1 ? AL_FORMAT_MONO16 : AL_FORMAT_STEREO16, pcm,info.samplerate);
	}
	
	public static void init()
	{
        device = alcOpenDevice((ByteBuffer)null);
        if (device == NULL) {
            throw new IllegalStateException("Failed to open an OpenAL device.");
        }

        ALCCapabilities deviceCaps = ALC.createCapabilities(device);

        if (!deviceCaps.OpenALC10) {
            throw new IllegalStateException();
        }
        
        context = alcCreateContext(device, (IntBuffer)null);
        
//        boolean useTLC = deviceCaps.ALC_EXT_thread_local_context && alcSetThreadContext(context);
//        if (!useTLC) {
//            if (!alcMakeContextCurrent(context)) {
//                throw new IllegalStateException();
//            }
//        }
        if (!alcMakeContextCurrent(context)) 
        {
        	throw new IllegalStateException();
        }
        
        caps = AL.createCapabilities(deviceCaps, MemoryUtil::memCallocPointer);
	}
	
	public static void stopContext()
	{
		alcDestroyContext(context);
		alcCloseDevice(device);
	}
	
	public void play_()
	{
		alSourcef(source,AL_PITCH,pitch); 
		alSourcef(source,AL_GAIN,gain);
		
		System.out.println((float)-Math.sqrt(1.0f - Math.pow(pan, pan)));
		
		alSource3f(source, AL_POSITION, pan, 0, (float)-Math.sqrt(1.0f - Math.pow(pan, pan)));
		
		//set up source input
        alSourcei(source, AL_BUFFER, buffer);
        
        isPlay = true;
        
        //play source
        alSourcePlay(source);
	}
	
	public void playAndWait()
	{
		play_();
		
		while (alGetSourcei(source, AL_SOURCE_STATE) != AL_STOPPED)
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

        stop();
	}
	
	public void play()
	{
		play_();

		new Thread(this::waitForEnd).start();
	}
	
	public void waitForEnd()
	{
		while (alGetSourcei(source, AL_SOURCE_STATE) != AL_STOPPED && isPlay)
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		isPlay = false;
		
        stop();
	}
	
	public void setPitch(float f)
	{
		pitch = f;
	}
	
	public void setGain(float f)
	{
		gain = Math.min(1.0f, f);
	}
	
	public void setPan(float f)
	{
		pan = f;
	}
	
	public float getPitch()
	{
		return pitch;
	}
	
	public float getGain()
	{
		return gain;
	}
	
	public float getPan()
	{
		return pan;
	}
	
	public void stop()
	{
		alSourcef(source,AL_PITCH,1f); 
		alSourcef(source,AL_GAIN,1f);
		
		alSource3f(source, AL_POSITION, 0, 0,-1);
		
		alSourceStop(source);
	}
	
	public void destroy()
	{
		alSourceStop(source);
        //delete buffers and sources
        alDeleteSources(source);

        alDeleteBuffers(buffer);
	}

}
