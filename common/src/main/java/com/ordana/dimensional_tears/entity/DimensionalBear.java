package com.ordana.dimensional_tears.entity;

import com.ordana.dimensional_tears.configs.CommonConfigs;
import com.ordana.dimensional_tears.reg.ModEntityTypes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.PolarBear;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class DimensionalBear extends PolarBear {
	public DimensionalBear(EntityType<? extends PolarBear> entityType, Level level) {
		super(entityType, level);
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0F).add(Attributes.FOLLOW_RANGE, 20.0F).add(Attributes.MOVEMENT_SPEED, 0.25F).add(Attributes.ATTACK_DAMAGE, 3.0F);
	}

	public static void spawnBear(ServerLevel serverLevel, double x, double y, double z, float xRot, float yRot) {
		if (serverLevel.getRandom().nextFloat() < CommonConfigs.BEAR_SPAWN_CHANCE.get() && serverLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
			var endermite = ModEntityTypes.DIMENSIONAL_BEAR.get().create(serverLevel);
			if (endermite != null) {
				endermite.moveTo(x, y, z, xRot, yRot);
				serverLevel.addFreshEntity(endermite);
			}
		}
	}

	public static void spawnBear(Entity entity) {
		var level = entity.level();
		var x =  entity.getX();
		var y = entity.getY();
		var z = entity.getZ();
		var yRot = entity.getYRot();
		var xRot = entity.getXRot();
		if (level instanceof ServerLevel serverLevel) {
			spawnBear(serverLevel, x, y, z, xRot, yRot);
		}
	}

	protected void registerGoals() {
		super.registerGoals();
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(1, new PolarBearMeleeAttackGoal());
		this.goalSelector.addGoal(1, new PanicGoal(this, 2.0F, (pathfinderMob) -> pathfinderMob.isBaby() ? DamageTypeTags.PANIC_CAUSES : DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES));
		this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25F));
		this.goalSelector.addGoal(5, new RandomStrollGoal(this, 1.0F));
		this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new PolarBearHurtByTargetGoal());
		this.targetSelector.addGoal(2, new DimensionalBearAttackPlayersGoal());
		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (player)->true));
		this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Fox.class, 10, true, true, null));
	}

	@Override
	public void aiStep() {
		if (this.level().isClientSide()) {
			for(int i = 0; i < 2; ++i) {
				this.level().addParticle(ParticleTypes.PORTAL, this.getRandomX(0.5F), this.getRandomY() - (double)0.25F, this.getRandomZ(0.5F), (this.random.nextDouble() - (double)0.5F) * (double)2.0F, -this.random.nextDouble(), (this.random.nextDouble() - (double)0.5F) * (double)2.0F);
			}
		}
		super.aiStep();
	}

	@Override
	public boolean isAngry() {
		return true;
	}

	@Override
	public boolean isAngryAt(LivingEntity livingEntity) {
		return true;
	}

	@Override
	public boolean isAngryAtAllPlayers(Level level) {
		return true;
	}

	@Override
	public boolean doHurtTarget(Entity entity) {
		boolean bl = super.doHurtTarget(entity);
		if (bl && entity instanceof LivingEntity livingEntity) {
			var level = this.level();
			if (!level.isClientSide) {
				for(int i = 0; i < 16; ++i) {
					double d = livingEntity.getX() + (livingEntity.getRandom().nextDouble() - (double)0.5F) * (double)16.0F;
					double e = Mth.clamp(livingEntity.getY() + (double)(livingEntity.getRandom().nextInt(16) - 8), level.getMinBuildHeight(), level.getMinBuildHeight() + ((ServerLevel)level).getLogicalHeight() - 1);
					double f = livingEntity.getZ() + (livingEntity.getRandom().nextDouble() - (double)0.5F) * (double)16.0F;
					if (livingEntity.isPassenger()) {
						livingEntity.stopRiding();
					}

					Vec3 vec3 = livingEntity.position();
					if (livingEntity.randomTeleport(d, e, f, true)) {
						level.gameEvent(GameEvent.TELEPORT, vec3, GameEvent.Context.of(livingEntity));
						SoundSource soundSource;
						SoundEvent soundEvent;
						if (livingEntity instanceof Fox) {
							soundEvent = SoundEvents.FOX_TELEPORT;
							soundSource = SoundSource.NEUTRAL;
						} else {
							soundEvent = SoundEvents.CHORUS_FRUIT_TELEPORT;
							soundSource = SoundSource.PLAYERS;
						}

						level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), soundEvent, soundSource);
						livingEntity.resetFallDistance();
						break;
					}
				}

				if (livingEntity instanceof Player player) {
					player.resetCurrentImpulseContext();
				}
			}
		}

		return bl;
	}


	@Override
	public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
		return null;
	}

	public class DimensionalBearAttackPlayersGoal extends NearestAttackableTargetGoal<Player> {
		public DimensionalBearAttackPlayersGoal() {
			super(DimensionalBear.this, Player.class, 20, true, true, null);
		}

		public boolean canUse() {
			return true;
		}

		protected double getFollowDistance() {
			return super.getFollowDistance() * (double)0.5F;
		}
	}
}
