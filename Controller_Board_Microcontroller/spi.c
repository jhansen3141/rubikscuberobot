//Josh Hansen
//Rubik's Cube Robot
//April 2016
#include "LPC13xx.h"
#include "spi.h"
#include "gpio.h"

void SPIInit() {

	LPC_SYSCON->PRESETCTRL &= ~0x1; // Reset SSP0
	LPC_SYSCON->PRESETCTRL |= 0x1; // Reset SSP0
	LPC_SYSCON->SYSAHBCLKCTRL |= 0x00000800; // enable sys clock for SSP0
	LPC_SYSCON->SSP0CLKDIV = 0x01; // divide clk by 1
	LPC_IOCON->PIO0_9 &= ~0x07;
	LPC_IOCON->PIO0_9 |= 0x01; // SSP0
    LPC_IOCON->PIO2_11 &= ~0x07;
    LPC_IOCON->PIO2_11 |= 0x01; // SCK0
    LPC_IOCON->SCKLOC = 0x01;
    LPC_IOCON->SCK_LOC = 0x01;
	LPC_IOCON->PIO0_2 &= ~ 0x07;
	LPC_IOCON->PIO0_2 |=  0x00;
	GPIOSetDir(0,2,1);
	GPIOSetValue(0,2,1);
	LPC_SSP->CR0 = (0x00000007 | 0x00000100); // 8 bits per xmit
	LPC_SSP->CPSR = 0x02; // CPSDVSR = 2, CLK = SYSCLK / SPSDVSR*(SCR+1)
	uint8_t i, Dummy=Dummy;
	for ( i = 0; i < 8; i++ ){
		Dummy = LPC_SSP->DR;
	}
	LPC_SSP->CR1 = (0x00000002);
}

void SPIWrite(uint8_t data) {
	uint8_t Dummy = Dummy;
	while(((LPC_SSP->SR) & (0x00000002 | 0x00000010 )) != 0x00000002);
	LPC_SSP->DR = data;
	while(((LPC_SSP->SR) & (0x00000010 | 0x00000004)) !=  0x00000004);

	Dummy = LPC_SSP->DR;
	return;
}

