package dev.moma.ecopicks.screen.custom;

import dev.moma.ecopicks.entity.custom.LeafeeEntity;
import dev.moma.ecopicks.item.ModItems;
import dev.moma.ecopicks.screen.ModScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

public class LeafeeScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final PropertyDelegate propertyDelegate;
    public final LeafeeEntity entity;

    public LeafeeScreenHandler(int syncId, PlayerInventory playerInventory, LeafeeEntity entity) {
        this(syncId, playerInventory, new SimpleInventory(7), entity);
    }

    public LeafeeScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, LeafeeEntity entity) {
        super(ModScreenHandlers.LEAFEE_SCREEN_HANDLER, syncId);
        this.inventory = inventory;
        this.entity = entity;
        checkSize(inventory, 7);
        inventory.onOpen(playerInventory.player);

        this.propertyDelegate = new PropertyDelegate() {
            private int progress = 0;

            @Override
            public int get(int index) {
                return this.progress;
            }

            @Override
            public void set(int index, int value) {
                this.progress = value;
            }

            @Override
            public int size() {
                return 1;
            }
        };
        this.addProperties(this.propertyDelegate);
        if (entity != null && !entity.getWorld().isClient()) {
            this.propertyDelegate.set(0, entity.getProgress());
        }

        this.addSlot(new Slot(inventory, 0, 75, 21) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(Items.AMETHYST_SHARD);
            }

            @Override
            public int getMaxItemCount() {
                return 3;
            }
        });
        this.addSlot(new Slot(inventory, 1, 136, 21) {
            @Override
            public boolean canInsert(ItemStack stack) {
                return false;
            }

            @Override
            public int getMaxItemCount() {
                return 3;
            }
        });

        for (int i = 0; i < 5; i++) {
            this.addSlot(new Slot(inventory, 2 + i, 75 + i * 18, 53));
        }

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }

        for (int col = 0; col < 9; col++) {
            this.addSlot(new Slot(playerInventory, col, 8 + col * 18, 142));
        }
    }

    public PropertyDelegate getPropertyDelegate() {
        return this.propertyDelegate;
    }

    @Override
    public void sendContentUpdates() {
        if (this.entity != null && !this.entity.getWorld().isClient()) {
            this.propertyDelegate.set(0, this.entity.getProgress());
        }
        super.sendContentUpdates();
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        ItemStack newStack = ItemStack.EMPTY;
        Slot slotObj = this.slots.get(slot);

        if (slotObj != null && slotObj.hasStack()) {
            ItemStack originalStack = slotObj.getStack();
            newStack = originalStack.copy();

            if (slot < this.inventory.size()) {
                if (!this.insertItem(originalStack, this.inventory.size(), this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.insertItem(originalStack, 0, this.inventory.size(), false)) {
                return ItemStack.EMPTY;
            }

            if (originalStack.isEmpty()) {
                slotObj.setStack(ItemStack.EMPTY);
            } else {
                slotObj.markDirty();
            }
        }

        return newStack;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }
}