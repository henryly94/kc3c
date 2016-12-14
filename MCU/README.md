MCU
===========

- MCU Type: TI-MSP430G2

- OS: ArchLinux

- IDE: Energia 1.6

---

## Setup

- [Setup Energia on Linux](http://energia.nu/guide/guide_linux/)

---

## Issues

**1. Permission denied while uploading**

```
rf2500: can't claim interface: permission denied
rf2500 : failed to open RF2500 device.
```

Run the IDE as root works. Otherwise, try to add current user to two usergroup and reboot may work (not for me).

```
sudo usermod -aG uucp <USERNAME>
sudo usermod -aG lock <USERNAME>
```

[Reference](https://bbs.archlinux.org/viewtopic.php?id=122180)
