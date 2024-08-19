//See LICENSE for license details.

package firesim.midasexamples

import midas._
import org.chipsalliance.cde.config._
import junctions._

import firesim.configs.{WithDefaultMemModel, WithWiringTransform}
import midas.models.{LatencyPipeConfig, BaseParams}

import firesim.lib.nasti.{NastiParameters}

class NoConfig extends Config(Parameters.empty)

// This is incomplete and must be mixed into a complete platform config
class BaseMidasExamplesConfig extends Config(
  new WithDefaultMemModel ++
  new WithWiringTransform ++
  new HostDebugFeatures ++
  new Config((_, _, _) => {
    case SynthAsserts => true
    case GenerateMultiCycleRamModels => true
    case EnableModelMultiThreading => true
    case EnableAutoILA => true
    case SynthPrints => true
    case EnableAutoCounter => true
  })
)

class DefaultF1Config extends Config(
  new BaseMidasExamplesConfig ++
  new midas.F1Config
)

class DefaultVitisConfig extends Config(
  new BaseMidasExamplesConfig ++
  new midas.VitisConfig
)

// used for PointerChaser testing
class PointerChaserLPC extends LatencyPipeConfig(BaseParams(16, 16))
class PointerChaserConfig extends Config((_, here, _) => {
  case MemSize => BigInt(1 << 30) // 1 GB
  case NMemoryChannels => 1
  case CacheBlockBytes => 64
  case CacheBlockOffsetBits => chisel3.util.log2Up(here(CacheBlockBytes))
  case NastiKey => NastiParameters(dataBits = 64, addrBits = 32, idBits = 3)
  case Seed => System.currentTimeMillis
})

// used for Printf testing
class AutoCounterPrintf extends Config((_, _, _) => {
  case AutoCounterUsePrintfImpl => true
})

// used for LoadMem testing
class LoadMemLPC extends LatencyPipeConfig(BaseParams(maxReads=16, maxWrites=16, beatCounters=true, llcKey=None))
class NoSynthAsserts extends Config((_, _, _) => {
  case SynthAsserts => false
})
