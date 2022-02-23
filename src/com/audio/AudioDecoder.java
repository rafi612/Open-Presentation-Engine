package com.audio;

import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.DecoderException;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.SampleBuffer;

public class AudioDecoder 
{
    public static class SoundInfo
    {
    	public int channels = 0,samplerate = 0;
    	public ShortBuffer buffer;
    }
    
    public static SoundInfo decodeMPEG(InputStream input) throws BitstreamException, DecoderException, IOException
    {
    	ArrayList<ShortBuffer> frames = new ArrayList<ShortBuffer>();
    	
    	Bitstream bitstream = new Bitstream(input);
    	Decoder decoder = new Decoder();
    	
    	SoundInfo info = new SoundInfo();
    	
    	System.out.println(info.samplerate);
    	
    	while (true)
    	{
    		Header header = bitstream.readFrame();
    		
    		if (header == null)
    			break;
    		
    		SampleBuffer sb = (SampleBuffer)decoder.decodeFrame(header, bitstream);
    		ShortBuffer sbuf = BufferUtils.createShortBuffer(decoder.getOutputBlockSize());
    		sbuf.put(sb.getBuffer());
    		sbuf.flip();
    		
    		frames.add(sbuf);
    		
    		bitstream.closeFrame();
    	}
    	
    	ShortBuffer buf = BufferUtils.createShortBuffer(frames.size() * decoder.getOutputBlockSize());
    	for (ShortBuffer sb : frames)
    		buf.put(sb);
    	frames.clear();
    	
    	input.close();
    	
    	buf.flip();
    	
    	info.channels = decoder.getOutputChannels();
    	info.samplerate = decoder.getOutputFrequency();
    	info.buffer = buf;
    	
    	return info;
    }
}
