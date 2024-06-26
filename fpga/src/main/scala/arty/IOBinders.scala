package chipyard.fpga.arty

import chisel3._

import freechips.rocketchip.devices.debug.{HasPeripheryDebug}

import chipyard.iobinders.{ComposeIOBinder, DebugResetPort, JTAGResetPort}

class WithDebugResetPassthrough extends ComposeIOBinder({
  (system: HasPeripheryDebug) => {
    // Debug module reset
    val io_ndreset: Bool = IO(Output(Bool())).suggestName("ndreset")
    io_ndreset := system.debug.get.ndreset

    // JTAG reset
    val sjtag = system.debug.get.systemjtag.get
    val io_sjtag_reset: Bool = IO(Input(Bool())).suggestName("sjtag_reset")
    sjtag.reset := io_sjtag_reset

    (Seq(DebugResetPort(() => io_ndreset), JTAGResetPort(() => io_sjtag_reset)), Nil)
  }
})
