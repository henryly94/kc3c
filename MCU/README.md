MCU
===========

- MCU Type: TI-MSP430G2

- OS: ArchLinux

- IDE: Energia 1.6


## Setup

- [Setup Energia on Linux](http://energia.nu/guide/guide_linux/)

## Issue

**1. Permission denied while uploading**

```
rf2500: can't claim interface: permission denied
rf2500: failed to open RF2500 device.
```

Run the IDE as root works. Otherwise, try to add current user to two usergroup and reboot may work (not for me). [[Forum Discussion]](https://bbs.archlinux.org/viewtopic.php?id=122180)

```
sudo usermod -aG uucp <USERNAME>
sudo usermod -aG lock <USERNAME>
```

---

## Reference

**Launchpad**

![launchpad](http://ohecbiy0g.bkt.clouddn.com/kc3c/launchpad.jpg)

**LM293**

![LM293](http://ohecbiy0g.bkt.clouddn.com/kc3c/LM293.png)
