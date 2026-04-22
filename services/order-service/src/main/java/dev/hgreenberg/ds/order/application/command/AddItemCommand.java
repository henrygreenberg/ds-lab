package dev.hgreenberg.ds.order.application.command;

import java.util.UUID;

public record AddItemCommand(UUID productId, int quantity) {}
