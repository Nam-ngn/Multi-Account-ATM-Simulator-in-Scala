# 💳 Multi-Account ATM Simulator in Scala

This project is a **command-line ATM (Automated Teller Machine) simulator** built in Scala. It supports **multiple users**, **persistent storage** via CSV, and basic banking operations with a secure PIN system.

## Features

- **Secure login** with ID + PIN (3 attempts max)
- **Multiple clients** managed via a `clients.csv` file
- Deposit money (CHF or EUR, with conversion)
- Withdraw money (with choice of large or small denominations)
- Check current account balance
- Change PIN securely
- Persistent save/load using `clients.csv`

---

## 🧪 Example Session

```bash
$ scala main.scala

Saisissez votre code identifiant > 1
Saisissez votre code pin > INTRO1235

Choisissez votre opération : 
   1) Dépôt 
   2) Retrait 
   3) Consultation du compte 
   4) Changement du code pin 
   5) Quitter
Votre choix : 3

Le montant disponible sur votre compte est de : 1200.00 CHF
