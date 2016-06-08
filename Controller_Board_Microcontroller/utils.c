#include "common.h"

int __sys_write (int iFileHandle, char *pcBuffer, int iLength)
{
	unsigned int i;
	for (i = 0;i< iLength; i++)
	{
		UARTSendChar(pcBuffer[i]); // print each character
	}
	return iLength;
}


// reverses a string 'str' of length 'len'
void reverse(unsigned char *str, int len)
{
    int i=0, j=len-1, temp;
    while (i<j)
    {
        temp = str[i];
        str[i] = str[j];
        str[j] = temp;
        i++; j--;
    }
}

 // Converts a given integer x to string str[].  d is the number
 // of digits required in output. If d is more than the number
 // of digits in x, then 0s are added at the beginning.
int intToStr(int x, unsigned char str[], int d)
{
    int i = 1;
    while (x)
    {
        str[i++] = (x%10) + '0';
        x = x/10;
    }

    // If number of digits required is more, then
    // add 0s at the beginning
    while (i < d)
        str[i++] = '0';

    reverse(str, i);
    str[i] = '\0';
    return i;
}

// Converts a floating point number to string.
void ftoa(float n, unsigned char *res, int afterpoint)
{
   if(n < 0) {
	    n = abs(n);
	    res[0] = '-';
   }
   else {
	   res[0] = '+';
   }
	// Extract integer part
    int ipart = (int)n;

    // Extract floating part
    float fpart = n - (float)ipart;

    // convert integer part to string
    int i = intToStr(ipart, res, 0);

    // check for display option after point
    if (afterpoint != 0)
    {
        res[i] = '.';  // add dot

        // Get the value of fraction part upto given no.
        // of points after dot. The third parameter is needed
        // to handle cases like 233.007
        fpart = fpart * pow(10, afterpoint);

        intToStr((int)fpart, res + i + 1, afterpoint);
    }
}

long map(long x, long in_min, long in_max, long out_min, long out_max) {
  return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
}


void delay_us(uint32_t delay) {

    LPC_TMR16B0->TCR = 0x02; // reset timer
    LPC_TMR16B0->PR  = 71;
    LPC_TMR16B0->MR0 = delay;
    LPC_TMR16B0->IR  = 0xff; // reset interrupts
    LPC_TMR16B0->MCR = 0x04; // stop timer on match
    LPC_TMR16B0->TCR = 0x01; // start timer
    while (LPC_TMR16B0->TCR & 0x01); // wait until timer finished
}

void delay_ms(uint32_t delay) {
	uint32_t i;
	for(i=0;i<delay;i++) {
		delay_us(1000);
	}
}

