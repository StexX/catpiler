# size: 240
.data
hp0: .space 4
hp1: .space 4
hp2: .space 4
hp3: .space 4
hp4: .space 4
hp5: .space 4
hp6: .space 4
hp7: .space 4
hp8: .space 4
hp9: .space 4
hp10: .space 4
hp11: .space 4
hp12: .space 4
hp13: .space 4
hp14: .space 4
.text
addi $v0 $zero 9
addi $a0 $zero 32
syscall
la $t0 hp0
sw $v0 ($t0)
addi $v0 $zero 9
addi $a0 $zero 32
syscall
la $t0 hp1
sw $v0 ($t0)
addi $v0 $zero 9
addi $a0 $zero 32
syscall
la $t0 hp2
sw $v0 ($t0)
addi $v0 $zero 9
addi $a0 $zero 32
syscall
la $t0 hp3
sw $v0 ($t0)
addi $v0 $zero 9
addi $a0 $zero 32
syscall
la $t0 hp4
sw $v0 ($t0)
addi $v0 $zero 9
addi $a0 $zero 32
syscall
la $t0 hp5
sw $v0 ($t0)
addi $v0 $zero 9
addi $a0 $zero 32
syscall
la $t0 hp6
sw $v0 ($t0)
addi $v0 $zero 9
addi $a0 $zero 32
syscall
la $t0 hp7
sw $v0 ($t0)
addi $v0 $zero 9
addi $a0 $zero 32
syscall
la $t0 hp8
sw $v0 ($t0)
addi $v0 $zero 9
addi $a0 $zero 32
syscall
la $t0 hp9
sw $v0 ($t0)
addi $v0 $zero 9
addi $a0 $zero 32
syscall
la $t0 hp10
sw $v0 ($t0)
addi $v0 $zero 9
addi $a0 $zero 32
syscall
la $t0 hp11
sw $v0 ($t0)
addi $v0 $zero 9
addi $a0 $zero 32
syscall
la $t0 hp12
sw $v0 ($t0)
addi $v0 $zero 9
addi $a0 $zero 32
syscall
la $t0 hp13
sw $v0 ($t0)
addi $v0 $zero 9
addi $a0 $zero 32
syscall
la $t0 hp14
sw $v0 ($t0)
main: 
add $fp $zero $sp
subu $sp $sp 4
addi $v0 $zero 5
syscall
lw $t0 0($fp)
move $t0 $v0
la $t1 hp0
lw $t1 ($t1)
addi $t2 $zero 99
sb $t2 ($t1)
addi $t1 $t1 1
addi $t2 $zero 97
sb $t2 ($t1)
addi $t1 $t1 1
addi $t2 $zero 108
sb $t2 ($t1)
addi $t1 $t1 1
addi $t2 $zero 99
sb $t2 ($t1)
addi $t1 $t1 1
addi $t2 $zero 117
sb $t2 ($t1)
addi $t1 $t1 1
addi $t2 $zero 108
sb $t2 ($t1)
addi $t1 $t1 1
addi $t2 $zero 97
sb $t2 ($t1)
addi $t1 $t1 1
addi $t2 $zero 116
sb $t2 ($t1)
addi $t1 $t1 1
addi $t2 $zero 105
sb $t2 ($t1)
addi $t1 $t1 1
addi $t2 $zero 110
sb $t2 ($t1)
addi $t1 $t1 1
addi $t2 $zero 103
sb $t2 ($t1)
addi $t1 $t1 1
addi $t2 $zero 32
sb $t2 ($t1)
addi $t1 $t1 1
addi $t2 $zero 102
sb $t2 ($t1)
addi $t1 $t1 1
addi $t2 $zero 97
sb $t2 ($t1)
addi $t1 $t1 1
addi $t2 $zero 99
sb $t2 ($t1)
addi $t1 $t1 1
addi $t2 $zero 117
sb $t2 ($t1)
addi $t1 $t1 1
addi $t2 $zero 108
sb $t2 ($t1)
addi $t1 $t1 1
addi $t2 $zero 116
sb $t2 ($t1)
addi $t1 $t1 1
addi $t2 $zero 121
sb $t2 ($t1)
addi $t1 $t1 1
addi $t2 $zero 32
sb $t2 ($t1)
addi $t1 $t1 1
addi $t2 $zero 111
sb $t2 ($t1)
addi $t1 $t1 1
addi $t2 $zero 102
sb $t2 ($t1)
addi $t1 $t1 1
addi $t2 $zero 32
sb $t2 ($t1)
addi $t1 $t1 1
sb $zero ($t1)
addi $v0 $zero 4
la $a0 hp0
lw $a0 ($a0)
syscall
addi $v0 $zero 1
move $a0 $t0
syscall
la $t3 hp1
lw $t3 ($t3)
addi $t4 $zero 58
sb $t4 ($t3)
addi $t3 $t3 1
addi $t4 $zero 32
sb $t4 ($t3)
addi $t3 $t3 1
sb $zero ($t3)
addi $v0 $zero 4
la $a0 hp1
lw $a0 ($a0)
syscall
subu $sp $sp 4
move $a0 $t0
move $s0 $t0
move $s7 $ra
move $s6 $fp
jal faculty
move $fp $s6
move $ra $s7
move $t0 $s0
add $sp $zero $fp
addi $v0 $zero 10
syscall
faculty: 
add $fp $zero $sp
move $t0 $a0
addi $v0 $zero 1
move $a0 $t0
syscall
subu $sp $sp 4
li $t1 1
beq $t0 $t1 br0
addi $v1 $zero 1
j br1
br0:
add $v1 $zero $zero
j br1
br1:
beq $zero $v1 br2
subi $v1 $t0 1
move $a0 $v1
subu $sp $sp 4
sw $s0 -4($fp)
subu $sp $sp 4
sw $s1 -8($fp)
subu $sp $sp 4
sw $s2 -12($fp)
subu $sp $sp 4
sw $s3 -16($fp)
subu $sp $sp 4
sw $s4 -20($fp)
subu $sp $sp 4
sw $s5 -24($fp)
subu $sp $sp 4
sw $s6 -28($fp)
subu $sp $sp 4
sw $s7 -32($fp)
move $s0 $t0
move $s7 $ra
move $s6 $fp
jal faculty
move $fp $s6
move $ra $s7
move $t0 $s0
lw $s6 -28($fp)
lw $s5 -24($fp)
lw $s4 -20($fp)
lw $s3 -16($fp)
lw $s2 -12($fp)
lw $s1 -8($fp)
lw $s0 -4($fp)
lw $s7 -32($fp)
j br2
j br2
br2:
add $sp $zero $fp
jr $ra
