package com.hospital.dto;

/**
 * Aggregate counts for the public landing page (no auth required).
 */
public record PublicStatsDTO(long doctors, long departments, long appointments, long patients) {}
