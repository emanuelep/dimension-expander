// Inherit methods from CroneEngine
Engine_DimExp : CroneEngine {
	// Define a getter for the synth variable
	var <synth;

	// Define a class method when an object is created
	*new { arg context, doneCallback;
		// Return the object from the superclass (CroneEngine) .new method
		^super.new(context, doneCallback);
	}

	// Rather than defining a SynthDef, use a shorthand to allocate a function and send it to the engine to play
	// Defined as an empty method in CroneEngine
	// https://github.com/monome/norns/blob/master/sc/core/CroneEngine.sc#L31
	alloc {
		// Define the synth variable, whichis a function
		synth = {
			// define arguments to the function
			arg out, multiplier=1, drywet=1, monoflag=0;
			// initialize local vars
			
      var leftch = SoundIn.ar(0);
      var delayed1_l = DelayC.ar(leftch,0.2,0.1120*multiplier,LFPar.ar(0.98)*0.5,0); //maximum amplitude is 0.5 and LFPar oscillates at 0.98 Hz between -1 and 1
      var delayed2_l = DelayC.ar(leftch,0.2,0.1327*multiplier,LFPar.ar(0.985)*0.5,0);  
      var delayed3_l = DelayC.ar(leftch,0.2,0.1572*multiplier,LFPar.ar(0.99)*0.5,0);  
      var delayed4_l = DelayC.ar(leftch,0.2,0.1810*multiplier,LFPar.ar(0.8237)*0.5,0); 
      var leftch_processed = Mix.new([delayed1_l, delayed2_l, delayed3_l, delayed4_l]); //leftch*0.8563, 
      
      var rightch = Mix.new([SoundIn.ar(1), SoundIn.ar(0)*monoflag])*(-1); // takes from right input by default, adds left ch in case right is dead/monostereo switch is pressed, then flips the phase
      
      // rightch = Mix.new([rightch, leftch*monoflag])*(-1); // flips the phase of one side and adds left channel in case right channel is dead/monostereo switch is pressed
      
      //if ( DetectSilence.kr(rightch), {rightch=leftch*(-1);}, {rightch=rightch;}); // turns mono to stereo in case of silence on the right channel, why not working!! damn
      
      var delayed1_r = DelayC.ar(rightch,0.2,0.1120*multiplier,LFPar.ar(0.98)*0.5,0); //maximum amplitude is 0.5 and LFPar oscillates at 0.98 Hz between -1 and 1
      var delayed2_r = DelayC.ar(rightch,0.2,0.1327*multiplier,LFPar.ar(0.985)*0.5,0);  
      var delayed3_r = DelayC.ar(rightch,0.2,0.1572*multiplier,LFPar.ar(0.99)*0.5,0);  
      var delayed4_r = DelayC.ar(rightch,0.2,0.1810*multiplier,LFPar.ar(0.8237)*0.5,0); 
      var rightch_processed = Mix.new([delayed1_r, delayed2_r, delayed3_r, delayed4_r]); //rightch*0.8563, 
      
      // var rightch_processed = leftch_processed;
  		
  		// Create an output object
			Out.ar(out, [ leftch_processed*drywet, rightch_processed*drywet ]);
			
			// Send the synth function to the engine as a UGen graph.
		// It seems like when an Engine is loaded it is passed an AudioContext
		// that is used to define audio routing stuff (Busses and Groups in SC parlance)
		// These methods are defined in 
		// https://github.com/monome/norns/blob/master/sc/core/CroneAudioContext.sc
		// pass the CroneAudioContext method "out_b" as the value to the \out argument
		// pass the CroneAudioContext method "xg" as the value to the target.
		}.play(args: [\out, context.out_b], target: context.xg);
		
	// Export argument symbols as modulatable paramaters
		// This could be extended to control the Lag time as additional params
		this.addCommand("multiplier", "f", { arg msg;
			synth.set(\multiplier, msg[1]);
		});
		
		this.addCommand("drywet", "f", { arg msg;
			synth.set(\drywet, msg[1]);
		});

		this.addCommand("monoflag", "f", { arg msg;
			synth.set(\monoflag, msg[1]);
		});
		
			}
	// define a function that is called when the synth is shut down
	free {
		synth.free;
	}
}