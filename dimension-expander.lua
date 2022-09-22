-- Dimension Expander
-- based on Xfer's VST Plugin:
-- four voices stereo spreader 
-- E2: Dry/Wet, E3: Size
-- K2: On/Off, K3: St/Mo->St
-- Mo->St takes left ch. input
-- @emanuelep

engine.name = 'DimExp'

function init()
  audio.monitor_stereo()
  engine.drywet(1);
  engine.multiplier(1);
  positionEnc2=100;
  positionEnc3=100;
  switch=1;
  monoflag=0; -- 0 = normal stereo, 1 = mono to stereo
  engine.monoflag(0)
end

function redraw()
  screen.aa(1)
  screen.clear()
  screen.level(15)
  screen.font_face(10)
  screen.font_size(13)
  screen.move(0,15)
  screen.text("Dry/Wet: " .. positionEnc2 .. "%")
  screen.move(0,30)
  screen.text("Size: " .. positionEnc3 .. "%")
  if switch == 1 then 
    screen.move(0,63)
    screen.text("ON")
  end
  if switch == 0 then 
    screen.move(0,63)
    screen.text("OFF")
  end
  if monoflag == 1 then 
    screen.move(33,63)
    screen.text("MO->ST")
  end
  if monoflag == 0 then 
    screen.move(33,63)
    screen.text("ST")
  end
  
  -- let's draw the chicken! starting from the beak
  screen.aa(1)
  screen.line_width(1)
  screen.move(88,40)
  screen.line(93,37)
  screen.line(93,36)
  screen.line(94,35)
  screen.line(95,36)
  screen.line(96,36)
  screen.line(96,35)
  screen.move(97,37)
  screen.line(99,40)
  screen.line(99,41)
  screen.line(102,44)
  screen.line(107,44)
  screen.move(107,46)
  screen.line(112,41)
  screen.line(113,42)
  screen.line(113,45)
  screen.line(112,46)
  screen.line(112,48)
  screen.line(111,49)
  screen.line(111,51)
  screen.line(110,52)
  screen.line(108,55)
  screen.line(107,55)
  -- belly
  screen.line(106,56)
  screen.line(101,56)
  screen.line(99,54)
  screen.line(98,54)
  screen.line(96,52)
  screen.line(96,50)
  screen.line(95,49)
  screen.line(95,48)
  screen.line(94,47)
  screen.line(94,46)
  screen.move(96,45)
  screen.line(95,46)
  screen.line(93,46)
  screen.line(88,40)
  -- beak
  screen.move(90,43)
  screen.line(90,38)
  -- legs
  screen.move(103,56)
  screen.line(103,59)
  screen.line(101,59)
  screen.move(106,56)
  screen.line(106,59)
  screen.line(104,59)
  -- eyes
  screen.move(93,40)
  screen.circle(93,40,0.1)
  screen.stroke()
  screen.update()
end


function enc(n,d)
  -- dry/wet
  if n == 2 then
    positionEnc2 = util.clamp(positionEnc2 + d,0,100)
    if switch == 1 then
      engine.drywet(positionEnc2/100)
    end
    redraw()
  end
  -- time delay, "size" in xfer's plugin
  if n == 3 then
    positionEnc3 = util.clamp(positionEnc3 + d,0,100)
    engine.multiplier(positionEnc3/100)
    redraw()
  end  
  
end


function key(n,z)
  -- on/off switch
  if n == 2 then
      if z == 1 then
        if switch == 0 then
          switch = 1
          engine.drywet(positionEnc2/100)
          if monoflag == 1 then
            audio.monitor_mono()
          end
        elseif switch == 1 then
          switch = 0
          engine.drywet(0)
          if monoflag == 1 then
            audio.monitor_stereo()
          end
        end
      end
    redraw()
  end
  -- mono to stereo switch  
  if n == 3 then
      if z == 1 then
        if monoflag == 0 then
          monoflag = 1
          engine.monoflag(1)
          if switch == 1 then
            audio.monitor_mono()
          end
        elseif monoflag == 1 then
          monoflag = 0
          engine.monoflag(0)
          audio.monitor_stereo()
        end
      end
    redraw()
  end
end