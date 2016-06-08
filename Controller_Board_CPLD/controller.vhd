--Josh Hansen
--Motor Controller Board Logic
--April 2016
library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;
USE ieee.std_logic_signed.all;

entity CONTROLLER is 
	port(
		osc_clk_in : in std_logic;
		sdi: in std_logic;
		s_clk: in std_logic;
		s_latch: in std_logic;
		
		motor_1_load: in std_logic;
		motor_2_load: in std_logic;
		motor_3_load: in std_logic;
		motor_4_load: in std_logic;
		
		motor_1_freq_out : out std_logic;	
		motor_1_enable : out std_logic;
		motor_1_dir : out std_logic;
		motor_1_mico : out std_logic_vector(2 downto 0);	
		
		motor_2_freq_out : out std_logic;
		motor_2_enable : out std_logic;
		motor_2_dir : out std_logic;
		motor_2_mico : out std_logic_vector(2 downto 0);
		
		motor_3_freq_out : out std_logic;
		motor_3_enable : out std_logic;
		motor_3_dir : out std_logic;
		motor_3_mico : out std_logic_vector(2 downto 0);	
		
		motor_4_freq_out : out std_logic;
		motor_4_enable : out std_logic;
		motor_4_dir : out std_logic;
		motor_4_mico : out std_logic_vector(2 downto 0);	
		
		servo_1_out: out std_logic;
		servo_2_out: out std_logic;
		servo_3_out: out std_logic;
		servo_4_out: out std_logic
		);
end CONTROLLER;

ARCHITECTURE arch of CONTROLLER IS
	
	constant SHIFT_BITS: integer := 24; -- size of the shift register
	constant SERVO_LOWER: integer := 1000;
	constant SERVO_UPPER: integer := 20000;
	
	signal clk_419KHz_speed: std_logic;
	signal motor_1_accum_reg: unsigned(15 downto 0) := "0000000000000000";
	signal motor_2_accum_reg: unsigned(15 downto 0) := "0000000000000000"; 
	signal motor_3_accum_reg: unsigned(15 downto 0) := "0000000000000000";
	signal motor_4_accum_reg: unsigned(15 downto 0) := "0000000000000000"; 
	
	signal accum_reg_419KHz_speed: unsigned(13 downto 0);
	signal accum_reg_1MHz: natural range 0 to 250000;

	signal shift_reg: std_logic_vector(SHIFT_BITS-1 downto 0);

	signal motor_1_accum_add_value: unsigned(15 downto 0) := "0000000000000000";
	signal motor_2_accum_add_value: unsigned(15 downto 0) := "0000000000000000";
	signal motor_3_accum_add_value: unsigned(15 downto 0) := "0000000000000000";
	signal motor_4_accum_add_value: unsigned(15 downto 0) := "0000000000000000";
	
	signal motor_1_freq_i: std_logic;
	signal motor_2_freq_i: std_logic;
	signal motor_3_freq_i: std_logic;
	signal motor_4_freq_i: std_logic;
	
	signal motor_1_target_pos:unsigned(15 downto 0) := "0000000000000000";
	signal motor_2_target_pos:unsigned(15 downto 0) := "0000000000000000";
	signal motor_3_target_pos:unsigned(15 downto 0) := "0000000000000000";
	signal motor_4_target_pos:unsigned(15 downto 0) := "0000000000000000";
	
	signal motor_1_actual_pos:unsigned(15 downto 0) := "0000000000000000";
	signal motor_2_actual_pos:unsigned(15 downto 0) := "0000000000000000";
	signal motor_3_actual_pos:unsigned(15 downto 0) := "0000000000000000";
	signal motor_4_actual_pos:unsigned(15 downto 0) := "0000000000000000";
	
	signal clk_25MHz_i: std_logic;
	signal clk_1MHz_i: std_logic;
	
	signal so_t : std_logic;
	signal so_temp : std_logic_vector(15 downto 0);
	
	signal data_1_reg : std_logic_vector(15 downto 0);
	signal data_2_reg : std_logic_vector(15 downto 0);
	signal data_3_reg : std_logic_vector(15 downto 0);
	signal data_4_reg : std_logic_vector(15 downto 0);
	
	type m1_state_type is (m1_running,m1_set);
	signal m1_state : m1_state_type := m1_set;
	
	type m2_state_type is (m2_running,m2_set);
	signal m2_state : m2_state_type := m2_set;
	
	type m3_state_type is (m3_running,m3_set);
	signal m3_state : m3_state_type := m3_set;
	
	type m4_state_type is (m4_running,m4_set);
	signal m4_state : m4_state_type := m4_set;
	
	signal servo_1_accum_reg: unsigned(14 downto 0);
	signal servo_1_pos_i: unsigned(10 downto 0);
	signal servo_1_pos: unsigned(9 downto 0) := "0111110100"; -- 500 mid position
	
	signal servo_2_accum_reg: unsigned(14 downto 0);
	signal servo_2_pos_i: unsigned(10 downto 0);
	signal servo_2_pos: unsigned(9 downto 0) := "0111110100"; -- 500 mid position
	
	signal servo_3_accum_reg: unsigned(14 downto 0);
	signal servo_3_pos_i: unsigned(10 downto 0);
	signal servo_3_pos: unsigned(9 downto 0) := "0111110100"; -- 500 mid position
	
	signal servo_4_accum_reg: unsigned(14 downto 0);
	signal servo_4_pos_i: unsigned(10 downto 0);
	signal servo_4_pos: unsigned(9 downto 0) := "0111110100"; -- 500 mid position
	
begin	
--------------------------------------------------------------------------------------	
	m1_state_machine:process(osc_clk_in)
	begin
		if(rising_edge(osc_clk_in)) then
			case m1_state is
				when m1_running =>
					motor_1_freq_out <= motor_1_freq_i;
				when m1_set =>
					motor_1_freq_out <= '0';
			end case;
		end if;
	end process;
--------------------------------------------------------------------------------------	
	m2_state_machine:process(osc_clk_in)
	begin
		if(rising_edge(osc_clk_in)) then
			case m2_state is
				when m2_running =>
					motor_2_freq_out <= motor_2_freq_i;
				when m2_set =>
					motor_2_freq_out <= '0';
			end case;
		end if;
	end process;
--------------------------------------------------------------------------------------	
	m3_state_machine:process(osc_clk_in)
	begin
		if(rising_edge(osc_clk_in)) then
			case m3_state is
				when m3_running =>
					motor_3_freq_out <= motor_3_freq_i;
				when m3_set =>
					motor_3_freq_out <= '0';
			end case;
		end if;
	end process;
--------------------------------------------------------------------------------------	
	m4_state_machine:process(osc_clk_in)
	begin
		if(rising_edge(osc_clk_in)) then
			case m4_state is
				when m4_running =>
					motor_4_freq_out <= motor_4_freq_i;
				when m4_set =>
					motor_4_freq_out <= '0';
			end case;
		end if;
	end process;
--------------------------------------------------------------------------------------	
	clk_50MHz_to_25MHz: process(osc_clk_in)
	begin
		if(rising_edge(osc_clk_in)) then
			clk_25MHz_i <= (NOT clk_25MHz_i);
		end if;
	end process;
---------------------------------------------------------------------------------------	
	clk_25Mhz_to_419KHz_speed: process(clk_25MHz_i)
	begin	
		if(rising_edge(clk_25MHz_i)) then
			accum_reg_419KHz_speed <= accum_reg_419KHz_speed + 275;			
		end if;
	end process;
	clk_419KHz_speed <= std_logic(accum_reg_419KHz_speed(13));	
---------------------------------------------------------------------------------------	
	clk_25MHz_to_1MHz: process(clk_25MHz_i)
	begin	
		if(rising_edge(clk_25MHz_i)) then
			if(accum_reg_1MHz = 24) then
				accum_reg_1MHz <= 0;
				clk_1MHz_i <= '1';
			else
				accum_reg_1MHz <= accum_reg_1MHz + 1;
			end if;
			if(clk_1MHz_i = '1') then
				clk_1MHz_i <= '0';
			end if;
		end if;
	end process;
---------------------------------------------------------------------------------------			
	motor_1_dds: process(clk_1MHz_i)
	begin
		if(rising_edge(clk_1MHz_i)) then
			motor_1_accum_reg <= motor_1_accum_reg + motor_1_accum_add_value;
		end if;
	end process;
	motor_1_freq_i <= std_logic(motor_1_accum_reg(15));	
---------------------------------------------------------------------------------------	
	motor_1_pos: process(motor_1_target_pos,motor_1_freq_i,motor_1_load)
	begin	
		if(motor_1_load = '1') then
			motor_1_actual_pos <= motor_1_target_pos;
		elsif(rising_edge(motor_1_freq_i)) then
			if(motor_1_actual_pos > 0) then	
				m1_state <= m1_running;
				motor_1_actual_pos <= motor_1_actual_pos - 1;
			else
				m1_state <= m1_set;
			end if;
		end if;
	end process;				
---------------------------------------------------------------------------------------	
	motor_2_dds: process(clk_1MHz_i)
	begin
		if(rising_edge(clk_1MHz_i)) then
			motor_2_accum_reg <= motor_2_accum_reg + motor_2_accum_add_value;
		end if;
	end process;
	motor_2_freq_i <= std_logic(motor_2_accum_reg(15));	
	---------------------------------------------------------------------------------------	
	motor_2_pos: process(motor_2_target_pos,motor_2_freq_i,motor_2_load)
	begin	
		if(motor_2_load = '1') then
			motor_2_actual_pos <= motor_2_target_pos;
		elsif(rising_edge(motor_2_freq_i)) then
			if(motor_2_actual_pos > 0) then	
				m2_state <= m2_running;
				motor_2_actual_pos <= motor_2_actual_pos - 1;
			else
				m2_state <= m2_set;
			end if;
		end if;
	end process;	
---------------------------------------------------------------------------------------	
	motor_3_dds: process(clk_1MHz_i)
	begin
		if(rising_edge(clk_1MHz_i)) then
			motor_3_accum_reg <= motor_3_accum_reg + motor_3_accum_add_value;
		end if;
	end process;
	motor_3_freq_i <=  std_logic(motor_3_accum_reg(15));
	---------------------------------------------------------------------------------------	
	motor_3_pos: process(motor_3_target_pos,motor_3_freq_i,motor_3_load)
	begin	
		if(motor_3_load = '1') then
			motor_3_actual_pos <= motor_3_target_pos;
		elsif(rising_edge(motor_3_freq_i)) then
			if(motor_3_actual_pos > 0) then	
				m3_state <= m3_running;
				motor_3_actual_pos <= motor_3_actual_pos - 1;
			else
				m3_state <= m3_set;
			end if;
		end if;
	end process;	
---------------------------------------------------------------------------------------	
	motor_4_dds: process(clk_1MHz_i)
	begin
		if(rising_edge(clk_1MHz_i)) then
			motor_4_accum_reg <= motor_4_accum_reg + motor_4_accum_add_value;
		end if;
	end process;
	motor_4_freq_i <=  std_logic(motor_4_accum_reg(15));	
	---------------------------------------------------------------------------------------	
	motor_4_pos: process(motor_4_target_pos,motor_4_freq_i,motor_4_load)
	begin	
		if(motor_4_load = '1') then
			motor_4_actual_pos <= motor_4_target_pos;
		elsif(rising_edge(motor_4_freq_i)) then
			if(motor_4_actual_pos > 0) then	
				m4_state <= m4_running;
				motor_4_actual_pos <= motor_4_actual_pos - 1;
			else
				m4_state <= m4_set;
			end if;
		end if;
	end process;
---------------------------------------------------------------------------------------	
	servo_1_pos_i <= unsigned('0' & servo_1_pos) + SERVO_LOWER;
	servo_1_pwm: process(clk_1MHz_i)
	begin
		if(rising_edge(clk_1MHz_i)) then
			if(servo_1_accum_reg = SERVO_UPPER) then
				servo_1_accum_reg <= (others => '0');
			else
				servo_1_accum_reg <= servo_1_accum_reg + 1;
			end if;
		end if;
	end process;
	servo_1_out <= '1' when (servo_1_accum_reg < servo_1_pos_i) else '0';
---------------------------------------------------------------------------------------	
	servo_2_pos_i <= unsigned('0' & servo_2_pos) + SERVO_LOWER;
	servo_2_pwm: process(clk_1MHz_i)
	begin
		if(rising_edge(clk_1MHz_i)) then
			if(servo_2_accum_reg = SERVO_UPPER) then
				servo_2_accum_reg <= (others => '0');
			else
				servo_2_accum_reg <= servo_2_accum_reg + 1;
			end if;
		end if;
	end process;
	servo_2_out <= '1' when (servo_2_accum_reg < servo_2_pos_i) else '0';
---------------------------------------------------------------------------------------	
	servo_3_pos_i <= unsigned('0' & servo_3_pos) + SERVO_LOWER;
	servo_3_pwm: process(clk_1MHz_i)
	begin
		if(rising_edge(clk_1MHz_i)) then
			if(servo_3_accum_reg = SERVO_UPPER) then
				servo_3_accum_reg <= (others => '0');
			else
				servo_3_accum_reg <= servo_3_accum_reg + 1;
			end if;
		end if;
	end process;
	servo_3_out <= '1' when (servo_3_accum_reg < servo_3_pos_i) else '0';
---------------------------------------------------------------------------------------	
	servo_4_pos_i <= unsigned('0' & servo_4_pos) + SERVO_LOWER;
	servo_4_pwm: process(clk_1MHz_i)
	begin
		if(rising_edge(clk_1MHz_i)) then
			if(servo_4_accum_reg = SERVO_UPPER) then
				servo_4_accum_reg <= (others => '0');
			else
				servo_4_accum_reg <= servo_4_accum_reg + 1;
			end if;
		end if;
	end process;
	servo_4_out <= '1' when (servo_4_accum_reg < servo_4_pos_i) else '0';
---------------------------------------------------------------------------------------		
	shift_register: process(s_clk)
	begin
		if(rising_edge(s_clk)) then
			shift_reg <= shift_reg(SHIFT_BITS-2 downto 0) & sdi;
		end if;
	end process;
---------------------------------------------------------------------------------------
	latch:process(s_latch)
	begin
		if(rising_edge(s_latch)) then
			case shift_reg(23 downto 16) is
				when "00000000" =>
				when "00000001" =>
					motor_1_accum_add_value <= unsigned(shift_reg(15 downto 0)); -- A1 is stepper motor 1 speed
				when "00000010" =>
					motor_2_accum_add_value <= unsigned(shift_reg(15 downto 0)); -- A2 is stepper motor 2 speed
				when "00000011" =>
					motor_3_accum_add_value <= unsigned(shift_reg(15 downto 0)); -- A3 is stepper motor 3 speed
				when "00000100" =>
					motor_4_accum_add_value <= unsigned(shift_reg(15 downto 0)); -- A4 is stepper motor 4 speed
				when "00000101" =>
					motor_1_mico <= std_logic_vector(shift_reg(2 DOWNTO 0)); -- A5 is stepper motor 1 micro step setting
				when "00000110" =>
					motor_2_mico <= std_logic_vector(shift_reg(2 DOWNTO 0)); -- A6 is stepper motor 2 micro step setting
				when "00000111" =>
					motor_3_mico <= std_logic_vector(shift_reg(2 DOWNTO 0)); -- A7 is stepper motor 3 micro step setting
				when "00001000" =>
					motor_4_mico <= std_logic_vector(shift_reg(2 DOWNTO 0)); -- A8 is stepper motor 4 micro step setting
				when "00001001" =>	
					motor_1_target_pos <= unsigned(shift_reg(15 downto 0)); -- A9 is stepper motor 1 position
				when "00001010" =>
					motor_2_target_pos <= unsigned(shift_reg(15 downto 0)); -- A10 is stepper motor 2 position
				when "00001011" =>
					motor_3_target_pos <= unsigned(shift_reg(15 downto 0)); -- A11 is stepper motor 3 position
				when "00001100" =>
					motor_4_target_pos <= unsigned(shift_reg(15 downto 0)); -- A12 is stepper motor 4 position
				when "00001101" =>
					motor_1_enable <= shift_reg(0); -- A13 is stepper motor 1 enable
				when "00001110" =>
					motor_2_enable <= shift_reg(0); -- A14 is stepper motor 2 enable
				when "00001111" =>
					motor_3_enable <= shift_reg(0); -- A15 is stepper motor 3 enable
				when "00010000" =>
					motor_4_enable <= shift_reg(0); -- A16 is stepper motor 4 enable
				when "00010001" =>
					servo_1_pos <= unsigned(shift_reg(9 downto 0)); -- A17 is servo 1 position
				when "00010010" =>
					servo_2_pos <= unsigned(shift_reg(9 downto 0)); -- A18 is servo 2 position
				when "00010011" =>
					servo_3_pos <= unsigned(shift_reg(9 downto 0)); -- A19 is servo 3 position
				when "00010100" =>
					servo_4_pos <= unsigned(shift_reg(9 downto 0)); -- A20 is servo 4 position
				when "00010101" =>
					motor_1_dir <= shift_reg(0); -- A21 is motor 1 direction
				when "00010110" =>
					motor_2_dir <= shift_reg(0); -- A22 is motor 2 direction
				when "00010111" =>
					motor_3_dir <= shift_reg(0); -- A23 is motor 3 direction
				when "00011000" =>
					motor_4_dir <= shift_reg(0); -- A24 is motor 4 direction
				when others =>
			end case;
		end if;
	end process;			
---------------------------------------------------------------------------------------	
end arch;