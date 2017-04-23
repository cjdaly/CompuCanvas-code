
# CompuCanvas code

## command line control

The USB devices incorporated into CompuCanvas systems have been selected (among other factors) because they can be controlled in a straightforward way from the command line on an rPi.  Below are command line examples for each device.

#### BlinkStick

Use the `blinkstick` command to control the BlinkStick lights.  Run `blinkstick --help` to see usage information.  Here are some example commands:

    blinkstick --pulse red
    blinkstick --index 0 --pulse red
    blinkstick --index 1 green

#### text to speech

Use the `espeak` command to make the CompuCanvas talk.  Note that this command emits some spurious errors which can be directed to `/dev/null` like this:

    espeak "Hello" 2>/dev/null

Run `espeak --help` to see usage information.

#### playing MP3s

The `mpg321` program has been installed for playing audio files.  Run `mpg321 --help` for usage information.  Given an audio file named `song.mp3`, play it with:

    mpg321 song.mp3

#### Show2 with Weatherboard

For CompuCanvas systems that incorporate the ODROID Show2 with WeatherBoard, see the [Show2-Eboogaloo repo](https://github.com/cjdaly/Show2-Eboogaloo).  When the Show2 is placed behind the canvas, the text needs to be _big_ to be readable.  It also helps to turn up the LCD backlight intensity.  For example:

    cd ~/Show2-Eboogaloo
    ./show2.sh siz7 blt255 fg2 /0/Hello fg3 /1/World

#### Maxbotix Ultrasonic Rangefinder

For CompuCanvas systems that incorporate a USB connected Maxbotix Ultrasonic Rangefinder (like [from Adafruit](https://www.adafruit.com/products/1343)), try this to see the raw range data:

    ( stty -F /dev/ttyUSB0 57600 ; cat /dev/ttyUSB0 )

## CompuCanvas Controller

The CompuCanvas Controller (C3) is a work in progress based on headless Eclipse 4.5 and Java 8 (included in Raspbian distribution).  Normally it will be started on bootup (see `/etc/rc.local` and the `sudo-setup.sh` script discussed below) and will monitor sensors, blink lights, play sounds and otherwise do 'interesting things' in a hopefully unobtrusive and non-annoying manner.  To check out C3:

    cd ~/CompuCanvas-code/CompuCanvas-controller/C3-runtime
    ./c3.sh status

Invoking `c3.sh` with no arguments will show a usage message which, in addition to `status` describes `start` and `stop` arguments. Check out the `c3.log` file for details on C3 operation.

## new system configuration

First, burn [Raspbian](https://www.raspberrypi.org/downloads/raspbian/) Jessie image onto a microSD, [configure for ssh](https://www.raspberrypi.org/blog/a-security-update-for-raspbian-pixel/), boot and login (`user:pi` ; `pw:raspberry`), then run the following command to set a new password:

    sudo passwd pi

Next, run `sudo raspi-config` and set timezone, etc.

Finally, clone the CompuCanvas code and run the system setup script:

    git clone https://github.com/cjdaly/CompuCanvas-code.git
    cd CompuCanvas-code/CompuCanvas-scripts/setup
    sudo ./sudo-setup.sh
    sudo reboot
    
When this reboot completes you should hear "Hello from CompuCanvas..." and see the BlinkStick lights in operation.
