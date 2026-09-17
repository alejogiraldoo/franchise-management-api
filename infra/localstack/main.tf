terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 6.0"
    }
  }
}

provider "aws" {
  region                      = "us-east-1"
  access_key                  = "test"
  secret_key                  = "test"
  skip_credentials_validation = true
  skip_requesting_account_id  = true
  skip_metadata_api_check     = true

  endpoints {
    secretsmanager = "http://localhost:4566"
    sts            = "http://localhost:4566"
  }
}

resource "aws_secretsmanager_secret" "database" {
  name                    = "franchise-hybrid/database"
  recovery_window_in_days = 0
}

resource "aws_secretsmanager_secret_version" "database" {
  secret_id = aws_secretsmanager_secret.database.id
  secret_string = sensitive(jsonencode({
    username = "franchise_app"
    password = "local-simulation-only-123"
    database = "franchise_system"
  }))
}
